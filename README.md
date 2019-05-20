# Workshop-Components

Treinamento Android Arquitecture Components


### NewsAPP - Preview do projeto

![Navigation](https://thumbs.gfycat.com/NeighboringPresentErne-size_restricted.gif)


# Lista de Exercícios do Codelab

![GitHub Logo](https://media.giphy.com/media/itOuxcFvgYjWE/giphy.gif)

Instruções: Criar uma nova branch a partir da **master** com o seguinte template: codelab/nome

Implementar as Instruções abaixo e abrir um pull-request para **master**

1 - Crie uma nova Tabela chamada **(LocalNews)** usando Room para Armazenar Notícias Locais inseridas pelo usuário

2 - Criar um Fragment que exiba uma lista (RecyclerView) que extrairá informações da tabela **(LocalNews)** (Lembre-se da arquitetura: ViewModel + LiveData + Repositório)

3 - Crie um novo Fragment que dê suporte ao usuário **(Layout com Input Widgets)** para adicionar uma nova Notícia Local **(Armazenar na nova Tabela criada no passo - 1)**

4 - Usar o **Navigation Component** para navegar até o novo Fragmento da Lista e da tela de inserir uma nova notícia 

5 - Usar o **WorkManager** para ler uma notícia aleatória da Tabela **(LocalNews)**, e exibir uma notificação para o Usuário. 

## Dicas 

O principal objetivo do Codelab é a exibição de uma Lista (RecyclerView) que extrai informações de uma tabela do Banco de Dados Room, usando a arquitetura descrita neste artigo: [Guia de Arquitetura - Android](https://developer.android.com/jetpack/docs/guide?hl=pt-br), com isso sua implementação deve conter um Fragment, ViewModel, LiveData, Repositório e etc. 

O uso do Navigation Component será aplicado para que o usuário possa navegar entre as diferentes telas do aplicativo para o novo Fragment criado por você, você está livre para implementar o acesso a esta nova tela por meio de **Menus da ActionBar**, **BottomNavigationBar**, ou **NavigationDrawer.**

# Fundamentos:  Criando nossa navegação

### Links Úteis

* [Adicionar um NavHostFragment ao XML](https://developer.android.com/guide/navigation/navigation-getting-started#add_a_navhostfragment_via_xml)
* [Criar um Gráfico de Navegação](https://developer.android.com/guide/navigation/navigation-getting-started#create-nav-graph)


### navigation -> new resource file -> navigation_graph.xml

![Navigation](https://uploaddeimagens.com.br/images/002/086/210/original/navigation.PNG?1556984979)


### adicionar o novo NavHostFragment (res -> layout -> activity_main.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".ui.BaseActivity">

    <fragment
            android:id="@+id/navHost"
            android:name="androidx.navigation.fragment.NavHostFragment"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:defaultNavHost="true"
            app:navGraph="@navigation/navigation_graph"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

### Arquivo de Navegação XML (res -> navigation -> navigation_graph.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            xmlns:tools="http://schemas.android.com/tools"
            android:id="@+id/navigation_graph"
            app:startDestination="@id/mainFragment">

    <fragment
            tools:layout="@layout/fragment_main"
            android:id="@+id/mainFragment"
            android:name="com.alifyz.newsapp.ui.main.MainFragment"
            android:label="MainFragment">
        <action
                android:id="@+id/action_mainFragment_to_detailsFragment"
                app:destination="@id/detailsFragment"
                app:enterAnim="@anim/nav_default_pop_enter_anim"
                app:exitAnim="@anim/nav_default_pop_exit_anim"
                />
    </fragment>

    <fragment
            tools:layout="@layout/fragment_details"
            android:id="@+id/detailsFragment"
            android:name="com.alifyz.newsapp.ui.details.DetailsFragment"
            android:label="DetailsFragment">
        <action
                android:id="@+id/action_detailsFragment_to_settingsFragment"
                app:destination="@id/settingsFragment"
                />
    </fragment>
    <fragment
            tools:layout="@layout/fragment_settings"
            android:id="@+id/settingsFragment"
            android:name="com.alifyz.newsapp.ui.settings.SettingsFragment"
            android:label="SettingsFragment"
            />
</navigation>
```

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


# Parte 5 - Configurando a MainViewModel

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

# Parte 6 - Criando nosso PageAdapter

### adapters -> NewsAdapter.kt

```kotlin
class NewsAdapter : PagedListAdapter<Article, NewsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNews = getItem(position)
        holder.title.text = currentNews?.title
        holder.description.text = currentNews?.description
        holder.imageView.loadRemote(currentNews?.urlToImage)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val imageView = itemView.findViewById<ImageView>(R.id.img_news)
        val title = itemView.findViewById<TextView>(R.id.txt_title)
        val description = itemView.findViewById<TextView>(R.id.txt_description)
        val group = itemView.findViewById<RelativeLayout>(R.id.viewgroup)
            .setOnClickListener(this)

        override fun onClick(v: View?) {
            val selectedNews = getItem(adapterPosition)
            val bundle = bundleOf(
                "id" to selectedNews?.id)
            v?.findNavController()?.navigate(R.id.action_mainFragment_to_detailsFragment, bundle)
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.content == newItem.content

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem
        }
    }
}
```

# Parte 7 - Configurando nossa UI Controller (MainFragment)

ui -> main -> MainFragment.kt

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

# Parte 8 - Configurando DetailsViewModel

### ui -> details -> DetailsViewModel.kt

```kotlin
class DetailsViewModel(applicationContext: Application)  : AndroidViewModel(applicationContext) {

    private val repository by lazy {
        AppRepository(applicationContext)
    }

    fun searchNews(id : Long) = repository.searchNews(id)

}
```

# Parte 9 - Configurando nossa UI Controller DetailsFragment

### ui -> details -> DetailsFragment.kt

```kotlin
class DetailsFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    lateinit var viewModel: DetailsViewModel
    lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getLong("id") ?: 0

        viewModel.searchNews(id).observe(this, Observer { currentArticle ->
            binding.news = currentArticle
            currentArticle.urlToImage?.let { url ->
                news_image.loadRemote(url)
            }
        })

        fab.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(binding.news?.url))
            startActivity(intent)
        }

        bar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.settings -> {
                findNavController().navigate(R.id.action_detailsFragment_to_settingsFragment)
                true
            }
            else -> {
                false
            }
        }
    }
}
```

# Parte 10 - Criando um Worker (Work Manager)

### background -> ImageProcessingWorker.kt

```kotlin
class ImageProcessingWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {

        makeStatusNotification("Processando Foto", applicationContext)

        return try {

            val picture = BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.galaxy
            )

            val blurImage = blurBitmap(picture, applicationContext)

            val fileUri = writeBitmapToFile(applicationContext, blurImage)

            makeStatusNotification("Foto Processada: $fileUri", applicationContext)

            Result.success()

        } catch (e: Exception) {

            Result.failure()
        }
    }
}
```


# Parte 11 - Configurando nosso SettingsViewModel

### ui -> Settings -> SettingsViewModel.kt

```kotlin
class SettingsViewModel : ViewModel() {

    private val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .setRequiresCharging(true)
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresStorageNotLow(true)
        .build()

    private val worker = OneTimeWorkRequestBuilder<ImageProcessingWorker>()
        .addTag("ImageProcessing")
        .setConstraints(constraints)
        .build()

    private val workStatus  : LiveData<WorkInfo>

    init {
        workStatus = WorkManager.getInstance().getWorkInfoByIdLiveData(worker.id)
    }

    fun startWorker() {
        WorkManager.getInstance().enqueue(worker)
    }

    fun workerStatus() = workStatus
}
```


# Parte 12 - Fonfigurando UI Controller SettingsFragment

### ui -> Settings -> SettingsFragment.kt

```kotlin
class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_work.setOnClickListener {
            viewModel.startWorker()
        }

        viewModel.workerStatus().observe(this, Observer {
            when (it.state) {
                WorkInfo.State.ENQUEUED -> {
                    Toast.makeText(context, "Work Agendado", Toast.LENGTH_SHORT).show()
                }

                WorkInfo.State.BLOCKED -> {
                    Toast.makeText(context, "Work Bloqueado", Toast.LENGTH_SHORT).show()
                }

                WorkInfo.State.CANCELLED -> {
                    Toast.makeText(context, "Work Cancelado", Toast.LENGTH_SHORT).show()
                }

                WorkInfo.State.FAILED -> {
                    Toast.makeText(context, "Work Falhou", Toast.LENGTH_SHORT).show()
                }

                WorkInfo.State.RUNNING -> {
                    Toast.makeText(context, "Work Rodando", Toast.LENGTH_SHORT).show()
                }

                WorkInfo.State.SUCCEEDED -> {
                    Toast.makeText(context, "Work Concluído", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
```
