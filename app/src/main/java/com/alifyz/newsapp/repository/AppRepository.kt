package com.alifyz.newsapp.repository

import android.content.Context

/**
 * Nosso repositório é a principal camada responsável por comunicar
 * com o Banco de Dados local e com nossas API remotas.
 * Dessa forma, para manipular e expor métodos para as camadas superiores
 * devemos ter acesso ao objeto Database do Room e a nossa API WebService
 */
class AppRepository(context : Context)