1. 你是一个后端工程师，现在已经有一个前端界面，需要提供后端接口获取数据，前端界面有下拉框可以选择停车策略，当前有三种策略，分别是Standard、Smart、SuperSmart；后端通过List\<String>类型返回这三种策略；通过GET /parking/strategy 接口调用

2. 前端界面需要展示停车场信息，有停车场容量，当前停的车辆的车牌号，对应车辆停在停车场的哪个位置，停车场应该有List\<Ticket>的数据，具体Ticket对象保存有对应停车位的position信息；通过GET /parking 接口调用；responseBody例子如下：

   ```json
   [
       {
           "id": 1,
           "name": "The Palza Park",
           "capacity": 9,
           "currentSlots": 1,
           "tickets": [
               {
                   "id": 4,
                   "parkingLotId": 1,
                   "plateNumber": "BC-1213",
                   "position": 1
               }
           ]
       },
       {
           "id": 2,
           "name": "City Mall Garage",
           "capacity": 12,
           "currentSlots": 3,
           "tickets": [
               {
                   "id": 1,
                   "parkingLotId": 2,
                   "plateNumber": "AB-1111",
                   "position": 1
               },
               {
                   "id": 2,
                   "parkingLotId": 2,
                   "plateNumber": "AC-1111",
                   "position": 2
               },
               {
                   "id": 3,
                   "parkingLotId": 2,
                   "plateNumber": "AC-1113",
                   "position": 3
               }
           ]
       },
       {
           "id": 3,
           "name": "Office Tower Parking",
           "capacity": 9,
           "currentSlots": 1,
           "tickets": [
               {
                   "id": 5,
                   "parkingLotId": 3,
                   "plateNumber": "AC-1000",
                   "position": 1
               }
           ]
       }
   ]
   ```

3. 添加park停车接口，通过POST /parking/park/{strategy} 接口调用，requestBody例子如下：

   ```json
   {
       "plateNumber": "AC-1000"
   }
   ```

   responseBody例子如下：

   ```json
   {
       "id": 1,
       "parkingLotId": 3,
       "plateNumber": "AC-1000",
       "position": 1
   }
   ```

4. 添加fetch停车接口，通过POST /parking/fetch 接口调用，requestBody例子如下：

   ```json
   {
       "plateNumber": "AB-1111"
   }
   ```

   responseBody例子如下：

   ```json
   {
       "id": 1,
       "parkingLotId": 2,
       "plateNumber": "AB-1111",
       "position": 1
   }
   ```

5. 将Car、Ticket、ParkingLot这三个类，放到数据中作为实体类；三者都是用Integer的id作为主键，Ticket和ParkingLot是多对一的关系

6. 将除了Park和Fetch接口的其他接口，使用ParkingService去替换ParkingBoy

7. 在ParkingService新建park方法，去替换ParkingBoy的park方法

8. 在ParkingService新建fetch方法，去替换ParkingBot的fetch方法

9. 添加跨域处理

10. 在项目启动时往ParkingLot数据表中放入三条数据

    ```json
    [
        {
            "name": "The Palza Park",
            "capacity": 9,
            "currentSlots": 0,
            "tickets": []
        },
        {
            "name": "City Mall Garage",
            "capacity": 12,
            "currentSlots": 0,
            "tickets": []
        },
        {
            "name": "Office Tower Parking",
            "capacity": 9,
            "currentSlots": 0,
            "tickets": []
        }
    ]
    ```

11. 将parkingBoyTest和parkingLotTest里的测试方法放到ParkingControllerTest进行重写

12. 将其余测试类方法放到ParkingControllerTest进行重写

