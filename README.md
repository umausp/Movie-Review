# Movie-Review
**Requirements** :  
  1. Show Movie List at Home Page which will have Image and Name of the movie.
  2. The Moview Name must be alphabatically sorted.
  3. Must be filterable by Movie Name
  4. Clicking on any Movie it should show the details scren
  5. On Details Scren there should be Bigger Imager and description and Review

**Key Technical Decisions** :
  1. MVVM Architecture
  2. Kotlin with Coroutine and Flow with Retry Logic
  3. Dagger Hilt for DI
  4. Fresco for Image Caching library
  5. ViewModel for lifecycle handling
  6. Room DB for Network Caching
  7. Timber for Logging
  9. Jetpack Paging 3.0

**Technnical Requirements** :     
  1. Data Model
  2. API Requirements
  3. Usage Estimations
  4. Data Optimization
  5. High Level Design 

1. **Data Model** :  
**Movie Table**

 id  |    title      |  review_by  |  headline | summary_short  | image_url  | external_link | opening_date  |
---- | ------------- | ---------- | ---------- | -------------- |----------- |-------------- |-------------- |
 Int |    String     |   String   |   String   |    String      |  String    |    String     |    String     |
 
2. **API Requirements**
  * Get Movie Review List

3. **Usage Estimations**
  * As this is Image related App downloading Images will require user's Internet Data
  * Let's suppose 20 Images are downloading at first API call and every image is of 500 KB then total ~10 MB User's data will be used(Same with JSON data)
  * If User Avarage Scroll is 5 times(100 Images and JSON data) then on every session ~25 MB of data used daily and monthly it will be ~750 MB(Which is huge)

4. **Data Optimization**  
  * Save Images locally by using Good Image Caching Library Fresco
  * Save Network Response at Local Database using **Room**
  
5. **System Diagram**  
  ![Exhaustive Design](http://www.plantuml.com/plantuml/png/ZP9DQxD04CVl-oa6dtFoq1mRA15ZKy7MOD90eI2RTJHBDxj1rpIbvBjtDqkhqHvwaPdvpVx7UPdLcbQwsGiYiD2W5LIyt6d8UOLCSoK9PLfLu0M2ezGUq1eAHYoaiXG86uxl09-4d4al-gXo50vrIBId9MOf7bJDZV27JG1e-ShVXDbzk0uUB37eVAsSXija5QsJz6KWFVVNldkQnSaY2jnx0fsK3dM50PeQ6QqHlAY0y7GmVUV_N-MJqk0VAHTqA_3IzNDm0_yENBvD1PTuQz8kndYhH3xLUD8sD52SKqq7GfC_2PslvnAccp3j_tEAHpj_VnbvHfCAB2j4wOHRSvmhhT69kx2JRzNZ9F3ZR6N6wY_2Vf5OCIeWe6o7fWeLdE5MDEYgEAjVn4Q_TqSA1gFHtUKcYFrOhxqGycFPU6U_FT0_7D9cMZxOFh4Biy1DphWJwrwtIXpy1SnGvirUV040)
   
   
6. **Bottle Necks**  
  * If our App reaches to meximum users then single server may not be enough
  * We may need multiple server and Load Balancer to handle them
  * We may need S3 bucket for Image saving
  * Message Queue like **Kafka** for inter server communication and handle request in Queues
  * We may Require Caching on server side too for response
  * On App side we may require to delete or cleaup or DB periodically in order order have memory available for app and make sure it is not exhausted by the DB alone





