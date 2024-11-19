# Project I from group 10 - Lập trình xây dựng chương trình quản lý dịch vụ Microsoft Team và đồng bộ dữ liệu về AirTable
#### Information
    1. Path: "src/main/java/infoTaskSchedule.json" chứa thông tin groupId, tableId (của AirTable)
    2. Path: "src/main/java/template/team_config/config.json" chứa thông tin CLIENT_ID, CLIENT_SECRET, TENANT_ID, OBJECT_ID (của Microsoft graph)
    3. Path: "src/main/java/template/service/airtable/configAirTable.json" chứa thông tin baseId, personal_access_token của AirTable
    
#### Configuration
    1. Download maven from https://maven.apache.org/download.cgi , then config this file in edit system environment variables (if you use window) to run the task schedule
###### Note: you dont need to config file config.json since this file already config
#### Set up automatical synchronous feature
    Step 1: open "laplich.bat" file in the repository and change the path to your java.exe, and change the path to "TaskScheduleForAntiRicons" class file
    ##### You can config task schedule for 2 way
    ###### First way 
    Step 2: open the task scheduler in the window search box
    Step 3: on the right hand box, click Create Task
    Step 4: enter the name of the task
    Step 5: at the Security options, choose "Run whether user is logged on or not" and select the "Do not store password ....". Finally, select "Run with highest privileges" box
    Step 6: at the Triggers window, click New and a New Trigger window will be popped up
    Step 7: at Settings section, select "Daily"
    Step 8: on the right handside of the Settings section, Change the time to what you desired and click "OK"
    Step 9: at the Actions wintdow, click New and a New Action window will be popped up
    Step 10: at the Action box, choose Start a program
    Step 11: at the Program/script box, enter the path to the "Syncronize.bat" file
    Step 12: hit "OK"
    Step 13: at the Actions section on the right handside click "Enable All Tasks History". Now every changes of this task will be logged at the History section
    ###### Second way
    Step 2: open cmnd , write this script schtasks /create /tn "MyNewTask" /tr "C:\Users\LamPhuss\Desktop\laplich.bat" /sc daily /st 08:00 /ru "SYSTEM"
# Usage <a name="usage"></a>
Find the "CLI.java" file and Run

# Contact information <a name="contact"></a>
Phạm Lâm Phú 20214969
Email: phu.pl214969@sis.hust.edu.vn

Nguyễn Thiện Sang 20214972
Email: sang.nt214972@sis.hust.edu.vn
