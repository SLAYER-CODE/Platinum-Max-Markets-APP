# Proyecto de mejora e inovacion
## Empresa: Platinum-MaxMarkets

**Estructura del aplicativo Utilizando la arquitectura _MVVM_**

### MVVM
> MVVM, con Data Binding en Android, tiene los beneficios de facilitar las pruebas y la modularidad, reduciendo a su vez la cantidad de código que tenemos > que escribir para conectar vista + modelo.
#### Model
> Lo mismo que para MVC y MVP. No hay cambios.

#### View

> La vista se vincula con variables "observables" y "acciones" expuestas por el ViewModel de forma flexible.

#### ViewModel
> Es el responsable de ajustar el modelo y preparar los datos observables que necesita la vista. También proporciona hooks para que la vista pase eventos al modelo. Sin embargo, el ViewModel no está vinculado a la vista.

## Lenguajes utilizados

1. Kotlin (Aplicativo)
2. GraphQl(SDL-Consultas)

## Librerias y depedencias

***FUNCIONAMIENTO***
- implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.4.1'
- implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1'
- implementation 'androidx.core:core-ktx:1.7.0'
- compileOnly 'com.github.pengrad:jdk9-deps:1.0'
- implementation 'androidx.appcompat:appcompat:1.1.0-alpha04'
- implementation 'androidx.legacy:legacy-support-v4:1.0.0'
- implementation "androidx.paging:paging-runtime-ktx:3.1.1"
- implementation "androidx.core:core-ktx:1.7.0"
- implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7"
- implementation platform('com.google.firebase:firebase-bom:28.4.0')
- implementation 'com.google.dagger:hilt-android:2.39.1'
- kapt 'com.google.dagger:hilt-compiler:2.39.1'

***CAMARA***
- implementation 'com.google.zxing:core:3.4.0'
- implementation 'com.journeyapps:zxing-android-embedded:4.2.0'
- implementation("androidx.camera:camera-core:1.0.1")
- implementation("androidx.camera:camera-camera2:1.0.1")
- implementation("androidx.camera:camera-lifecycle:1.0.1")
- implementation("androidx.camera:camera-view:1.0.0-alpha28")
- implementation("androidx.camera:camera-extensions:1.0.0-alpha28")

***CONECTIVIDAD***
- implementation("com.squareup.okhttp3:okhttp:4.9.3")
- implementation "com.squareup.retrofit2:retrofit:2.9.0"
- implementation "com.squareup.retrofit2:converter-gson:2.9.0"
- implementation 'com.google.firebase:firebase-analytics-ktx'
- implementation 'com.google.android.gms:play-services-auth:19.2.0'
- implementation 'com.facebook.android:facebook-android-sdk:[8,9)'
- implementation 'com.google.firebase:firebase-auth-ktx'
- implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.17'
- implementation("com.apollographql.apollo3:apollo-runtime:3.2.1")
- implementation("com.apollographql.apollo3:apollo-api:3.2.1")
- implementation("com.apollographql.apollo3:apollo-idling-resource:3.2.1")
- implementation 'com.facebook.shimmer:shimmer:0.5.0'

***ALMACENAMIENTO***
- implementation files('/home/slayer/Descargas/mongo-java-driver-3.2.0-SNAPSHOT.jar')
- implementation "androidx.room:room-runtime:2.4.2"
- implementation "androidx.room:room-ktx:2.4.2"
- implementation "androidx.datastore:datastore-preferences:1.0.0-alpha04"
- implementation('org.mongodb:mongo-java-driver:4.6.0')
- implementation 'org.mongodb:mongodb-driver-sync:4.6.0'

***TESTING***
- testImplementation 'junit:junit:4.13.2'
- testImplementation 'androidx.test.ext:junit:1.1.3'
- testImplementation 'androidx.test.espresso:espresso-core:3.4.0'
- testImplementation "io.mockk:mockk:1.12.2"

***DISEÑO***
- implementation 'com.google.android.material:material:1.5.0'
- implementation 'androidx.constraintlayout:constraintlayout:2.1.3'
- implementation 'androidx.navigation:navigation-fragment-ktx:2.4.2'
- implementation 'androidx.navigation:navigation-ui-ktx:2.4.2'
- implementation 'com.github.bkhezry:android-image-picker:1.4.0'
- implementation 'androidx.fragment:fragment-ktx:1.4.1'
- implementation 'com.klinkerapps:android-chips:1.3.1@aar'
- implementation 'com.eightbitlab:blurview:1.6.6'
- implementation 'com.squareup.picasso:picasso:2.71828'
- implementation 'jp.wasabeef:picasso-transformations:2.4.0'
- implementation 'com.github.demoNo:ParallaxViewPager:v1.0.0'
- implementation 'com.github.ybq:parallaxviewpager:2.0.0'
- implementation 'com.github.deano2390:FlowTextView:2.0.5'
- implementation 'com.github.mohammadatif:Animatoo:master'
- implementation 'jp.co.cyberagent.android:gpuimage:2.1.0'
- implementation 'com.github.rubensousa:gravitysnaphelper:2.2.2'

 
## Capturas principales del aplicativo.




### Manual instructivo de uso del aplicativo

