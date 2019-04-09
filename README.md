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

**OS X & Linux (also works on Windows):**

1. Download [BlackBird-1.1.0-RELEASE.jar](/release/BlackBird-1.1.0-RELEASE.jar) (or see all published versions [here](/release)).
2. Open terminal window.
3. Copy and paste this command in the terminal: 
```sh
java -jar "{path to directory}/BlackBird-1.1.0-RELEASE.jar"
```

## How to use 

For step by step instructions, go to [Wiki](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-58/wikis/home).

## Development setup

Clone the repository into the project folder.  `git clone https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-58.git`  
Download maven from apache's website. https://maven.apache.org/  
Download and install all required dependencies with maven. `mvn install`  
To compile and run the application: `mvn clean compile exec:java`  
To run unit-tests: `mvn clean test` 

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
