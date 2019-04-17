# Blackbird
> Blackbird is a university's new best friend when it comes to scheduling, deliveries of exercises, and announcements!

<img src="https://svgur.com/i/CVv.svg">
<img src="https://svgur.com/i/CX8.svg">

Blackbird is a learning management system (LMS) that makes scheduling easier for professors, assistants and student. It also has an assignment system that makes deliveries and grading simpler than ever.
In addition, Blackbird has functionality that lets professors post announcements for either assistants only, or for everyone to see. Restrictions are set depending on your role in a course. 

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
#### Installation
Download maven from [Apache's offical site](https://maven.apache.org/  ).

#### Setup
Clone the repository into the project folder:  
`git clone https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-58.git`

Navigate to project directory and download required dependencies:  
`mvn install`

#### Use
To compile and run the application:  
`mvn clean compile exec:java`  
    
To run test suite:  
`mvn clean test` 

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

## Licence

Blackbird LMS is released under the MIT licence.  
See [LICENSE](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-58/blob/master/LICENSE) for more information.

<!--## Meta

TDT4140 - Programvareutvikling

"Planlegge og administrere små programvareutviklings-prosjekter og delta som designer / programmerer / tester i større programvareprosjekter."

[GitLab: Gruppe 58](https://gitlab.stud.idi.ntnu.no/programvareutvikling-v19/gruppe-58/)

--><!--## Contributing

1. Fork it (<https://github.com/yourname/yourproject/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request -->
