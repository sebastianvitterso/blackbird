# Blackbird
> Blackbird is a university's new best friend when it comes to scheduling, deliveries of exercises, and announcements!

[![pipeline](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-58/badges/master/pipeline.svg)](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-58/commits/master)

[![coverage](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-58/badges/master/coverage.svg)](http://programvareutvikling-v19.pages.stud.idi.ntnu.no/gruppe-58/index.html)

Blackbird is a learning management system (LMS) that makes scheduling easier for every professor, student-assistent, and student. It also has an assignment system that makes deliveries and grading simplier than ever.
In addition to that, Blackbird has functionality that lets professors post announcements for either student-assistant only, or for everyone to see. Restrictions are set depending on your role in a course. 

<img src="https://i.imgur.com/jlE8Np0.png" width="800">

## Installation

**Windows:**

1. Download [BlackBird-1.1.0-RELEASE.jar](/release/BlackBird-1.1.0-RELEASE.jar) (or see all published versions [here](/release)).
2. Open file by double clicking on the file.
3. Enjoy! 

**OS X & Linux (fungerer også på Windows):**

1. Download [BlackBird-1.1.0-RELEASE.jar](/release/BlackBird-1.1.0-RELEASE.jar) (or see all published versions [here](/release)).
2. Open terminal window.
3. Copy and paste this command in the terminal: 
```sh
java -jar "{path to directory}/BlackBird-1.1.0-RELEASE.jar"
```

## How to use 

For step by step instructions, go to [Wiki](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-58/wikis/home).

## Development setup

Describe how to install all development dependencies and how to run an automated test-suite of some kind. Potentially do this for multiple platforms.

```sh
make install
npm test
```

## Release History

* 1.1.0
    * ADD: Assignment/submission-functionality.
    * ADD: Announcement-functionality.
    * ADD: Course member-list functionality.
* 1.0.0
    * Initial version.
    * ADD: Admin view.
    * ADD: User login with roles.
    * ADD: Calendar functionality.

## Meta

TDT4140 - Programvareutvikling

"Planlegge og administrere små programvareutviklings-prosjekter og delta som designer / programmerer / tester i større programvareprosjekter."

[GitLab: Gruppe 58](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-58/)

<!--## Contributing

1. Fork it (<https://github.com/yourname/yourproject/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request -->

<!-- Markdown link & img dfn's -->
[npm-image]: https://img.shields.io/npm/v/datadog-metrics.svg?style=flat-square
[npm-url]: https://npmjs.org/package/datadog-metrics
[npm-downloads]: https://img.shields.io/npm/dm/datadog-metrics.svg?style=flat-square
[travis-image]: https://img.shields.io/travis/dbader/node-datadog-metrics/master.svg?style=flat-square
[travis-url]: https://travis-ci.org/dbader/node-datadog-metrics
[wiki]: https://github.com/yourname/yourproject/wiki


** GUIDE TIL README.MD: ** 

_https://medium.com/@meakaakka/a-beginners-guide-to-writing-a-kickass-readme-7ac01da88ab3_

** TEMPLATE FULGT HER: **

_https://github.com/dbader/readme-template/blob/master/README.md_