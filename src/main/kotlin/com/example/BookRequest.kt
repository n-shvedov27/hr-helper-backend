package com.example

import io.ktor.server.application.*
import kotlinx.serialization.Serializable
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

@Serializable
data class BookRequest(
    val name: String
)

@Serializable
data class BookResponse(
    val id: Long,
    val name: String
)

@Serializable
data class ErrorResponse(val message: String)

interface Book : Entity<Book> {
    companion object : Entity.Factory<Book>()

    val id: Long?
    var name: String
}

object Books : Table<Book>("book") {
    val id = long("id").primaryKey().bindTo(Book::id)
    val name = varchar("name").bindTo(Book::name)
}

class BookService(private val environment: ApplicationEnvironment) {

    private val database = Database.connect(
        url = environment.config.property("db.url").getString(),
        driver = environment.config.property("db.driver").getString(),
        user = environment.config.property("db.user").getString(),
        password = environment.config.property("db.password").getString()
    )

//    private val database = Database.connect(
//        url = "jdbc:postgresql://localhost:5438/postgres",
//        driver = "org.postgresql.Driver",
//        user = "postgres",
//        password = "postgres"
//    )

    fun createBook(bookRequest: BookRequest): Boolean {
        val newBook = Book {
            name = bookRequest.name
        }

        val affectedRecordsNumber =
            database.sequenceOf(Books)
                .add(newBook)

        return affectedRecordsNumber == 1
    }

    fun findAllBooks(): Set<Book> =
        database.sequenceOf(Books).toSet()

    fun findBookById(bookId: Long): Book? =
        database.sequenceOf(Books)
            .find { book -> book.id eq bookId }

    fun updateBookById(bookId: Long, bookRequest: BookRequest): Boolean {
        val foundBook = findBookById(bookId)
        foundBook?.name = bookRequest.name

        val affectedRecordsNumber = foundBook?.flushChanges()

        return affectedRecordsNumber == 1
    }
    fun deleteBookById(bookId: Long): Boolean {
        val foundBook = findBookById(bookId)

        val affectedRecordsNumber = foundBook?.delete()

        return affectedRecordsNumber == 1
    }
}