# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [sweepstakes-0.0.37] - 2022-12-02
### :sparkles: New Features
- [`20e0bb6`](https://github.com/kettoleon/sweepstakes/commit/20e0bb69c8b4bbc19c86428119b0f92fe91d04a3) - Surprise *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.36] - 2022-12-02
### :bug: Bug Fixes
- [`52470ba`](https://github.com/kettoleon/sweepstakes/commit/52470ba9894408799a1c3dd132111ca464521a2d) - Considering only the fixtures of group phase *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.35] - 2022-11-30
### :sparkles: New Features
- [`7881071`](https://github.com/kettoleon/sweepstakes/commit/7881071be6fc89c0b5dbd92b13cebca2f3596687) - Showing people mathematically out of the classification in a bootstrap muted style *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.34] - 2022-11-30
### :sparkles: New Features
- [`a3ed1b4`](https://github.com/kettoleon/sweepstakes/commit/a3ed1b4f051c229fe3fb953712644e43e46be460) - Trying directly into prod a better way to guesstimate the amount of remaining match minutes of a day, when matches overlap in time. *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.33] - 2022-11-21
### :sparkles: New Features
- [`ea0f6f5`](https://github.com/kettoleon/sweepstakes/commit/ea0f6f5dd1e9e27ea76969f85deedb86444af8cf) - Showing the last updated time and the next expected time in the leaderboard, but testing it directly in production during a match :crossed_fingers: *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.32] - 2022-11-21
### :bug: Bug Fixes
- [`a3e90f3`](https://github.com/kettoleon/sweepstakes/commit/a3e90f3a30f6870f1653ffc73c743c48e3a6a141) - Saved the response to the filesystem but forgot to update the in-memory cache :roll_eyes: :facepalm: *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.31] - 2022-11-21
### :wrench: Chores
- [`e9e81e2`](https://github.com/kettoleon/sweepstakes/commit/e9e81e2bf8f7232bcc664af2bb4e24f24f304c38) - Investigating how the data is not being updated live during a match *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.29] - 2022-11-20
### :bug: Bug Fixes
- [`fc59fab`](https://github.com/kettoleon/sweepstakes/commit/fc59fab596571ca83335c60bb9f3d3368b93d284) - Trying to disable thymeleaf cache, as data is updated behind the scenes, but not in the frontend. *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.28] - 2022-11-20
### :bug: Bug Fixes
- [`db06ea6`](https://github.com/kettoleon/sweepstakes/commit/db06ea67fe63cc40713cf49cece350db17f98e48) - null€ *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.27] - 2022-11-20
### :bug: Bug Fixes
- [`edfcadb`](https://github.com/kettoleon/sweepstakes/commit/edfcadb3c7603c9948c29afb8b86d6b8dc43cd41) - The server had a different timezone... *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.25] - 2022-11-15
### :wrench: Chores
- [`30553da`](https://github.com/kettoleon/sweepstakes/commit/30553da8f26a2836a7974f2c6773e620fdd59939) - Trying to fix build in github actions *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.23] - 2022-11-15
### :sparkles: New Features
- [`b6c5363`](https://github.com/kettoleon/sweepstakes/commit/b6c5363d551740b211622f1e90d242d57839f56d) - Calls to the actual Football Rest API, with adaptive cache TTL during the matches *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.22] - 2022-11-14
### :sparkles: New Features
- [`3bdc6dd`](https://github.com/kettoleon/sweepstakes/commit/3bdc6dddb170ce3ef4aae81965f1e905ef59f5b2) - Switched from verse to Bizum *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.21] - 2022-11-14
### :bug: Bug Fixes
- [`f80ece3`](https://github.com/kettoleon/sweepstakes/commit/f80ece3f093c9831e7a3a56acd5c628435159d6d) - Fixed bug when setting an empty/null bet. *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.20] - 2022-11-14
### :bug: Bug Fixes
- [`41fdbc3`](https://github.com/kettoleon/sweepstakes/commit/41fdbc3449d0f7b06baf4367eddad8aa9491b0f9) - Enabled CSRF protection again. *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.19] - 2022-11-14
### :bug: Bug Fixes
- [`60dc1b5`](https://github.com/kettoleon/sweepstakes/commit/60dc1b56f2286bb32f42c3fc0931a7fb5b966192) - Fixed link to changelog. *(commit by [@kettoleon](https://github.com/kettoleon))*


## [sweepstakes-0.0.18] - 2022-11-14
### :sparkles: New Features
- [`661bd2e`](https://github.com/kettoleon/sweepstakes/commit/661bd2ed6f5b8b4fd40c1934456dcab8ec2d0b2e) - Adding CHANGELOG.md into the release process. *(commit by [@kettoleon](https://github.com/kettoleon))*


[sweepstakes-0.0.18]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.17...sweepstakes-0.0.18
[sweepstakes-0.0.19]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.18...sweepstakes-0.0.19
[sweepstakes-0.0.20]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.19...sweepstakes-0.0.20
[sweepstakes-0.0.21]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.20...sweepstakes-0.0.21
[sweepstakes-0.0.22]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.21...sweepstakes-0.0.22
[sweepstakes-0.0.23]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.22...sweepstakes-0.0.23
[sweepstakes-0.0.25]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.24...sweepstakes-0.0.25
[sweepstakes-0.0.27]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.26...sweepstakes-0.0.27
[sweepstakes-0.0.28]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.27...sweepstakes-0.0.28
[sweepstakes-0.0.29]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.28...sweepstakes-0.0.29
[sweepstakes-0.0.31]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.30...sweepstakes-0.0.31
[sweepstakes-0.0.32]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.31...sweepstakes-0.0.32
[sweepstakes-0.0.33]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.32...sweepstakes-0.0.33
[sweepstakes-0.0.34]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.33...sweepstakes-0.0.34
[sweepstakes-0.0.35]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.34...sweepstakes-0.0.35
[sweepstakes-0.0.36]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.35...sweepstakes-0.0.36
[sweepstakes-0.0.37]: https://github.com/kettoleon/sweepstakes/compare/sweepstakes-0.0.36...sweepstakes-0.0.37