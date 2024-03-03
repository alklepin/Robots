let pom = open pom.xml
let a =  ($pom.content | filter {|x| $x.tag == version})
let version: string = $a.content.0.content.0
let file = $"target/robots-proj-($version).jar"

java -cp $file robots.gui.RobotsProgram