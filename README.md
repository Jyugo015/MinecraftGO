# :robot:*Guideline for Setting Up MySQL*:robot:
## Things need to be downloaded
  -	mysql installer https://dev.mysql.com/downloads/installer/
      -	choose mysql-installer-web-community-8.0.37.0.msi
   	  -	No thanks just start my download 
  -	mysql connector jar file https://dev.mysql.com/downloads/connector/j/
      -	operating system: platform independent 
      -	choose mysql-connector-j-8.4.0.zip
   	  -	No thanks just start my download 

### mysql installer
  -	during the installation process(configuration part), u r required to create ur own username and password 
>[!WARNING]
>make sure u rmb it cuz u will use it later for database 

### mysql workbench 
  -	go to disk c(if u din modify the location of the installer to install mysql) -> Program Files -> MySQL -> MySQL Workbench -> MySQLWorkbench.exe 
  -	at the home page, create a connection 
  -	insert username and password using the one u inserted during the time u install mysql installer 
      -	hostname localhost, port 3306
  ![To create a connection](https://github.com/Jqyay/Database/blob/main/Screenshot%20(838).png)
  ![Configure liddis](https://github.com/Jqyay/Database/blob/main/Screenshot%20(839).png)
  -	need to create a schema named minecraft 
  ![Press this button](https://github.com/Jqyay/Database/blob/main/Screenshot%20(840).png)
  - press Apply 
## To gain connection to mysql to netbeans/vs code
### in netbeans 
  -	go to Libraries columns of ur project 
  -	right click on it 
  -	choose Add JAR/Folder
  -	go to desktop n search where ur mysql connector jar file is saved 
  -	extract the zip file 
  -	back to netbeans, choose the mysql-connector-j-8.?.? jar file(not the whole file)
  
### in vs code 
  - create a no-archetype project
  - press app file under the src section
  - java project de panel will appear 
  - go to the Reference Library to add the mysql connector jar file

another way for setting up the environment of the database(in vs code) is to use extension and set the password and username in where i forgot le hhh :joy_cat:
https://www.youtube.com/watch?v=wzdCpJY6Y4c
