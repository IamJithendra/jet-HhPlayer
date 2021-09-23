<h1>HhPlayer</h1></br>
<p>  
A demo film and television app using compose based on modern Android tech-stacks and MVVM architecture. Fetching data from the network and integrating persisted data in the database via repository pattern.
Declarative UI version of the film and television using compose.
</p>
</br>

<p>
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
</p>

## Download
Go to the [Releases](https://github.com/yellowhai/HhPlayer/releases) to download the latest APK.

## Screenshots
<p align="center">
<img src="/hhcp_screenshots1.gif" width="32%"/>
<img src="/hhcp_screenshots2.gif" width="32%"/>
<img src="/hhcp_screenshots3.gif" width="32%"/>
</p>

## Tech stack & Open-source libraries
- Minimum SDK level 21
- 100% [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- JetPack
  - Compose - A modern toolkit for building native Android UI.
  - LiveData - notify domain layer data to views.
  - Lifecycle - dispose observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - App Startup - Provides a straightforward, performant way to initialize components at application startup.
  - Paging3 - Paging list data display
  - workManager - backstage data update 
- Architecture
  - MVVM Architecture (Declarative View - ViewModel - Model)
  - Repository pattern
- Material Design & Animations
- [Accompanist](https://github.com/google/accompanist) - A collection of extension libraries for Jetpack Compose.
- [Landscapist](https://github.com/skydoves/landscapist) - Jetpack Compose image loading library with shimmer & circular reveal animations.
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - construct the REST APIs and paging network data.
- [XXPermissions](https://github.com/getActivity/XXPermissions) - Permission request framework
- [Material Dialogs](https://github.com/afollestad/material-dialogs) - User dialog box display
- [LitePal](https://github.com/guolindev/LitePal) - Local database management
- [AgentWeb](https://github.com/Justson/AgentWeb) - Web video player


# License
```xml
Designed and developed by 2021 yellowhai (https://github.com/yellowhai/HhPlayer)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
