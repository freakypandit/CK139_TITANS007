# Indoor Navigation App
*SIH2020 code repository for team TITANS007*

**Day 1** 
---
**Module 1**
**Feature 1 : Received Signal Strength Based Indoor Positioning**  
Our app utilizes the strength of the received signal to obtain the users’ position. This feature allows the user to precisely navigate to a specific person who is connected to the hotspot of his mobile using wifi and can be used when there are a lot of people in a given area eg. in a hall , places with multiple counters etc.

![RSSI Gif](https://github.com/freakypandit/CK139_TITANS007/blob/master/Resources/RSSI%20Distance%20Model/animation.gif?raw=true)

- **Feature 2 : Pressure Value Based Altitude Positioning**  
The value of the pressure sensor (already present in our smart phones) is used to accurately measure the altitude at which the device currently is. This feature helps in determining the floor at which the user is. 

![RSSI App](https://github.com/freakypandit/CK139_TITANS007/blob/master/Resources/RSSI%20Distance%20Model/WhatsApp%20Image%202020-08-02%20at%201.53.53%20PM.jpeg?raw=true)  

![RSSI App](https://github.com/freakypandit/CK139_TITANS007/blob/master/Resources/RSSI%20Distance%20Model/WhatsApp%20Image%202020-08-02%20at%201.53.53%20PM%20(1).jpeg?raw=true)

**Day 2**
---
- **Feature 1 : Augmented reality based end-to-end Navigation system**  
Our augmented reality powered system allows us to display navigation path on top of real world map using mobile devices. We are using Google's ARcore to provide flexible and scalable AR based solution, which helps us to achieve cutting edge precision, and faster response time. Our solution smartly reduces the decision space by taking user inputs at each point of iteration to provide faster path resolution resulting in higher throughput, for this purpose we are using optical labels to assist AR navigation. 
![AR+OpticalLabel Gif](https://github.com/freakypandit/CK139_TITANS007/blob/master/Resources/AR%20%2B%20Optical%20Label%20Flow/GIF%20Representation%20.gif?raw=true)

- **Feature 2 : Using Sensor information to refine navigation route**  
To cope with layout changes and to create more precise navigation routes, we will use mobile sensors to caputure path information while using AR navigation, and these path information can be fed to our system that tries to converge our proximity to create more efficient navigation routes.

- **Feature 3 : Altitude and Floor Identification using Pressure sensors**  
We have also implemented an altitude and floor identification feature, which helps us to answers queries regarding the position of users in respects to floors, so with this information we can identify, at what floor a user is.
