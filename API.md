1. Get Parking Strategies
    - Endpoint: GET /parking/strategy
    
    - Description: Retrieves a list of available parking strategies.
    
    - Response: 
    
         ```json
         [    
             "SuperSmart",
             "Standard",
             "Smart"
         ]
         ```
    
         


2. Get Parking Lots
    - Endpoint: GET /parking
    - Description: Retrieves a list of all parking lots.
    - Response:
      ```json
    [
          {
      		"id": 1,
      		"name": "Parking Lot 1",
      		"capacity": 100,
    		"availablePositions": 50
    	},
      	{
              "id": 2,
              "name": "Parking Lot 2",
              "capacity": 200,
            "availablePositions": 150
           }
      ]
      ```
    
      
    
3. Park a Car
    - Endpoint: POST /parking/park/{strategy}
    - Description: Parks a car using the specified strategy.
    - Request Body:
      ```json
    {
      	"plateNumber": "ABC123"
      }
      ```

  - Response:
    ``` json
    {
          "id": 1,
          "plateNumber": "ABC123",
          "parkingLotId": 1
      }
      ```
    
      
    
4. Fetch a Car
    - Endpoint: POST /parking/fetch
    - Description: Fetches a car using the provided ticket information.
    - Request Body:
      ```json
    {
        "ticketId": 1,
      	"plateNumber": "ABC123"
      }
      ```

  - Response:
    ```json
    {
          "id": 1,
          "plateNumber": "ABC123",
          "parkingLotId": 1
      }
      ```
    
      
    
5. Add a Parking Lot
    - Endpoint: POST /parking
    - Description: Adds a new parking lot.
    - Request Body:
      ```json
    {
        "name": "New Parking Lot",
      	"capacity": 300
      }
      ```
  - Response:
    ```json
    {
        "id": 3,
          "name": "New Parking Lot",
          "capacity": 300,
          "availablePositions": 300
      }
      ```
    
      