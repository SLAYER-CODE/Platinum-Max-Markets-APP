# Proyecto de mejora e innovación 

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

  
## Librerías y dependencias 

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

## Estructura del aplicativo:

![image](https://user-images.githubusercontent.com/51754456/172648625-81224484-5f68-4b4b-838e-97bf7b0709f3.png)
![image](https://user-images.githubusercontent.com/51754456/172650676-82984ac0-e14e-4142-9093-61d4bb284126.png)
![image](https://user-images.githubusercontent.com/51754456/172650778-8e1a28c2-4d83-44f1-ac08-96785b07112a.png)
![image](https://user-images.githubusercontent.com/51754456/172650838-d2efb519-9736-4107-8300-1875922274ae.png)
![image](https://user-images.githubusercontent.com/51754456/172650885-6dc437ed-bd59-46b4-9d6e-b2431e14ef11.png)
![image](https://user-images.githubusercontent.com/51754456/172651014-47a8bf33-15be-4fb4-83ec-8c541eea9f80.png)
![image](https://user-images.githubusercontent.com/51754456/172651120-b857d3ac-7e4e-4096-840a-1e8d467fea7c.png)



## Capturas principales del aplicativo.

**Pantallas de precentación**

![image](https://user-images.githubusercontent.com/51754456/172529491-ed5cc0fb-7490-4b78-b74d-f1c21b84f3e7.png)
![image](https://user-images.githubusercontent.com/51754456/172529525-f6af3acb-ab30-45bf-8710-20920b45487e.png)
![image](https://user-images.githubusercontent.com/51754456/172529546-c5022f5b-6b0a-43c8-b1d4-83af5fcb0a3a.png)

**Pantalla de inicio de sessión**

![image](https://user-images.githubusercontent.com/51754456/172529601-f6e2761f-3d52-4a54-acbe-30098b164395.png)

### Google

![image](https://user-images.githubusercontent.com/51754456/172529682-cd5bd3a5-69e2-4463-9ca0-e09a3ad7f1ff.png)

### Facebook

![image](https://user-images.githubusercontent.com/51754456/172529734-df64ab76-6cb5-4557-8c58-8b084a1a990c.png)

**Home**

- Lista de productos en el dispositivo
- 
![image](https://user-images.githubusercontent.com/51754456/172529770-bca0b13b-b60f-4820-a87e-21cea305fdd5.png)

- LIsta de productos servidor

![image](https://user-images.githubusercontent.com/51754456/172530055-f99f65f2-438b-4c08-81c2-793f9b8b3b63.png)

- Lista de productos Base de datos MongoDB

![image](https://user-images.githubusercontent.com/51754456/172530110-f588bc24-112b-4e03-8b78-6134bf7d935a.png)

**Agregación de productos**

QR:


![image](https://user-images.githubusercontent.com/51754456/172530176-5b6ed638-783d-47a9-9511-76fa73d949c3.png)

IMAGENES:

![image](https://user-images.githubusercontent.com/51754456/172530424-13ecaf85-de61-4e8c-ae2a-d9766e565bf2.png)
![image](https://user-images.githubusercontent.com/51754456/172530310-6d7071de-5868-4e5d-93a6-4abeacec1406.png)

**Cuadro de navegación**

![image](https://user-images.githubusercontent.com/51754456/172530493-be8f3f85-33e7-4cd1-9379-b6d4ddbdff47.png)

**Permisos**

![image](https://user-images.githubusercontent.com/51754456/172531018-3ddc11da-a522-456b-8930-059290751302.png)

**CLientes (P2P)**

![image](https://user-images.githubusercontent.com/51754456/172531293-7231b88a-4408-4a38-bffd-62173fcf9fd2.png)

**Interfaces del cliente**

![image](https://user-images.githubusercontent.com/51754456/172531354-48369001-1002-47d5-94c6-bc4a2e20f6d8.png)

**Lista de Productos**

***Productos del servidor***

![image](https://user-images.githubusercontent.com/51754456/172531651-a6a6cf2a-5380-432a-be2d-4ec2906ac68b.png)

***Asistentes***

![image](https://user-images.githubusercontent.com/51754456/172531756-8b5af648-0069-4c61-8061-7bcb43597583.png)

## Themas del aplicativo

Modo Oscuro

![image](https://user-images.githubusercontent.com/51754456/172623483-ce7dc764-3235-4825-a8de-791aa9d832ea.png)


# Caracteristicas y elementos

**Configuracion de visualisación**

![image](https://user-images.githubusercontent.com/51754456/172623624-5374541c-216a-495c-b167-3774268c3a64.png)

- GridLayout

![image](https://user-images.githubusercontent.com/51754456/172623724-3fcca2a0-9b7e-4edc-be4c-bba7dfe6f589.png)

- ListView

![image](https://user-images.githubusercontent.com/51754456/172623810-92978a1b-e9a2-485e-a324-8e74f835c412.png)

- Details

![image](https://user-images.githubusercontent.com/51754456/172623864-9376ea07-26c1-4ce3-ab9f-621bc70b5353.png)

- GridListView

![image](https://user-images.githubusercontent.com/51754456/172623933-8d6bd347-8bf0-4701-8939-a75fe7873848.png)

Permiso de camara

![image](https://user-images.githubusercontent.com/51754456/172624003-62d122fe-6a2f-462e-9d00-ea2d17db6b6f.png)


Camara en Fragmentos

![image](https://user-images.githubusercontent.com/51754456/172624177-c8475a65-d0b6-4a3a-bcb5-8597339a6b0f.png)





