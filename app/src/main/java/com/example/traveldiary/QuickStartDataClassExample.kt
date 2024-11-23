
//import io.github.cdimascio.dotenv.dotenv
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.toList
import org.bson.Document
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


data class Movie(@BsonId val id: ObjectId, val title: String, val genres: List<String>)

suspend fun main() {

    // Replace the placeholder with your MongoDB deployment's connection string
    val uri = System.getenv("MONGO_URI")
    val mongoClient = MongoClient.create(uri)

    // Vyberte databázi
    val databaseName = "diary"
    val database = mongoClient.getDatabase(databaseName)

    println("Kolekce v databázi '$databaseName':")
    val collectionNames = database.listCollectionNames().toList()
    println(collectionNames)
    for (collectionName in collectionNames) {
        println("- $collectionName")
    }

    /*
    //ulozeni dokumentu do databaze
    val collection = database.getCollection<Movie>("countries")
    try {
        val result = collection.insertOne(
            Movie(ObjectId(), "Ski Bloopers", listOf("Documentary", "Comedy"))
        )
        println("Success! Inserted document id: " + result.insertedId)
    } catch (e: MongoException) {
        System.err.println("Unable to insert due to an error: $e")
    }
     */

    // Vyberte kolekci 'countries'
    val collection: MongoCollection<Document> = database.getCollection("countries")
    try {
        // Fetch documents from the collection
        val documents = collection.find().toList()

        // Print the fetched documents
        if (documents.isEmpty()) {
            println("No documents found in the collection.")
        } else {
            println("Documents in the collection:")
            for (document in documents) {
                println(document.toJson())
            }
        }
    } catch (e: Exception) {
        println("An error occurred: ${e.message}")
    } finally {
        // Close the MongoDB client
        mongoClient.close()
    }


    mongoClient.close()

}

