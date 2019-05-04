# MaximaTech-Components
maxAcademy - Treinamento Android Arquitecture Components

# Parte 1 - Configurando API Key

## build.gradle (app-level)

Configurar os BuildConfigFields de acordo com a API Key gerada pelo site https://newsapi.org/

```groovy
   buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            buildConfigField("String", 'API_KEY', "\"8e19c72fab8047669c2357e8a288ffa6\"")
        }

        debug {
            buildConfigField("String", 'API_KEY', "\"8e19c72fab8047669c2357e8a288ffa6\"")
        }
    }
```


# Parte 2 - Usando Room Persistence Library

## Definindo os Modelos e Entidades (Tabelas de nosso Banco Room)

### data -> entity 

```kotlin
@Entity(tableName = "articles", indices = arrayOf(Index(value = ["content"], unique = true)))
data class Article (
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,
    @Nullable
    var author: String? = "",
    @ColumnInfo(name = "content")
    @Nullable
    var content: String? = "",
    @Nullable
    var description: String? = "",
    @Nullable
    var publishedAt: String? = "",
    @Nullable
    var title: String? = "",
    @Nullable
    var url: String? = "",
    @Nullable
    var urlToImage: String? = "",
    @Ignore
    var source: Source? = null
)
```

```kotlin
@Entity(tableName = "data")
data class Data(
    @PrimaryKey(autoGenerate = false)
    var id : Long = 1,
    var lastPage : Int)
```

```kotlin
@Entity(tableName = "sources")
data class Source(
    @PrimaryKey(autoGenerate = true)
    var newsId: Int,
    @SerializedName("id")
    var sourceId: String?,
    @SerializedName("name")
    var name: String?
)
```

```kotlin
data class News (
    var status : String?,
    var totalResults : String?,
    var articles : List<Article>?)
```

## Definindo nosso objeto DAO (Data Acess Object)

### data -> dao -> NewsDao.kt

```kotlin
@Dao
interface NewsDao {

    @Query("SELECT * FROM articles")
    fun loadAllPaginadedNews() : DataSource.Factory<Int, Article>

    @Query("SELECT * from data")
    fun loadPagingData() : Data

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePageToken(data : Data)

    @Query("SELECT * from articles where id = :id")
    fun searchNews(id : Long) : LiveData<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewSource(source : Source)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewArticle(article : Article)

    @Transaction
    fun insertNewsAndSource(article : Article, source : Source) {
        addNewArticle(article)
        addNewSource(source)
    }
}
```

## Criando nosso Banco Room

## data -> AppDatabase.kt

```kotlin
@Database(entities = [Article::class, Source::class, Data::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun DAO(): NewsDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildInstance(context)
            }
        }

        private fun buildInstance(context: Context): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, "database.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
```
# Parte 3 - Criando nosso Repositório

## repository -> AppRepository.kt

```kotlin
/**
 * Nosso repositório é a principal camada responsável por comunicar
 * com o Banco de Dados local e com nossas API remotas.
 * Dessa forma, para manipular e expor métodos para as camadas superiores
 * devemos ter acesso ao objeto Database do Room e a nossa API WebService
 */
class AppRepository(context : Context) {

    //Instância do Banco Room
    private val database : AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    //Instância do Retrofit
    private val webService : NewsApi by lazy {
        ApiFactory.newsApi
    }

    //Busca por uma Notícia específica no Banco por ID
    fun searchNews(id : Long) = database.DAO().searchNews(id)

    //Método responsável por conectar a API e inserir mais notícias no banco.
    fun fetchHeadlinesFromNetwork(pageId : Int = 1) {
        CoroutineScope(Dispatchers.IO).launch {
            val endpoint = webService.fetchNews(pageToken = pageId.toString())
            withContext(Dispatchers.Main){
                val request = endpoint.await()
                if(request.isSuccessful) {
                    storeHeadlinesFromNetwork(request.body())
                } else {
                    Log.e("Status: ${request.code()}:", request.message())
                }
            }
        }
    }

    //Método usado para Armazenar Notícias e Fontes em forma de Transaction
    fun addNewsAndSources(article : Article, source : Source) {
        CoroutineScope(Dispatchers.IO).launch {
            database.DAO().insertNewsAndSource(article, source)
        }
    }

    //Usado pelo PagingCallback para retornar a última página visualizada.
    fun getPaginationData() : Data?  = runBlocking{
        CoroutineScope(Dispatchers.IO).async {
            database.DAO().loadPagingData()
        }.await()
    }

    //Usado pelo PagingCallback para salvar a última página visualizada.
    fun savePaginationData(data : Data) {
        CoroutineScope(Dispatchers.IO).launch {
            database.DAO().savePageToken(data)
        }
    }

    /**
     * Usado pelo Método abaixo para Armazenar Itens no Banco após serem baixados da API
     * @see fetchHeadlinesFromNetwork
     */
    private fun storeHeadlinesFromNetwork(news : News?){
        val articles = news?.articles
        articles?.map {article ->
           addNewsAndSources(article, article.source ?:
           Source(newsId = article.hashCode(), sourceId = "Desconhecido", name = "Desconhecido"))
        }
    }
}
```


# Parte 4 - Configurando nosso Pagination

## Configurando as propriedades do Paging Library

```kotlin
class PaginationUtils {

    companion object {

        private const val DEFAULT_PAGINATION_SIZE = 20
        private const val DEFAULT_PRETECH_DISTANCE = 5

        val pageConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(DEFAULT_PAGINATION_SIZE)
            .setInitialLoadSizeHint(DEFAULT_PAGINATION_SIZE)
            .setPrefetchDistance(DEFAULT_PRETECH_DISTANCE)
            .build()
    }
}
```

## Configurando o PagingCallback 

```kotlin
class PagingCallback(val repository: AppRepository) : PagedList.BoundaryCallback<Article>() {

    var pageData = repository.getPaginationData()
    var pageToken = pageData?.lastPage ?: 1

    override fun onZeroItemsLoaded() {
        refreshPageToken()
        repository.fetchHeadlinesFromNetwork(pageToken)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Article) {
        pageToken++
        savePageToken(pageToken)
        repository.fetchHeadlinesFromNetwork(pageToken)
    }

    override fun onItemAtFrontLoaded(itemAtFront: Article) {
        refreshPageToken()
    }

    private fun refreshPageToken() {
        pageToken = repository.getPaginationData()?.lastPage ?: 1
    }

    private fun savePageToken(currentPageToken : Int) {
        val data = Data(1, currentPageToken)
        repository.savePaginationData(data)
    }
}
```

## Alterando o AppRepository.kt para expor e construir a lista paginada para ViewModel. 

```kotlin
 //Retorna a DataSource do Banco Room para construir a lista paginada.
    fun getPaginatedHeadlines() : LiveData<PagedList<Article>> {

        val dataSource = database.DAO().loadAllPaginadedNews()

        val pagingCallback = PagingCallback(this)

        val data = LivePagedListBuilder(dataSource, PaginationUtils.pageConfig)
            .setBoundaryCallback(pagingCallback)
            .build()

        return data
    }
```


# Parte 5 - Configurando a MainViewModel & nossa UI Controller MainFragment

### ui -> main -> MainViewModel.kt

```kotlin
class MainViewModel(
    applicationContext: Application
) : AndroidViewModel(applicationContext) {

    private val repository by lazy {
        AppRepository(applicationContext)
    }

    fun getPaginadedNews() : LiveData<PagedList<Article>> {
        return repository.getPaginatedHeadlines()
    }
}
```
### ui -> main -> MainFragment.kt

```kotlin
class MainFragment : Fragment() {

    private val adapter = NewsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.getPaginadedNews().observe(this, Observer {
            adapter.submitList(it)
        })

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }
}
```
