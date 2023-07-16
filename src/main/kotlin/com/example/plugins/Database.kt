//package com.example.plugins
//
//import io.ktor.server.application.*
//import org.jetbrains.exposed.dao.IntEntity
//import org.jetbrains.exposed.dao.IntEntityClass
//import org.jetbrains.exposed.dao.id.EntityID
//import org.jetbrains.exposed.dao.id.IntIdTable
//import org.jetbrains.exposed.sql.Database
//import org.jetbrains.exposed.sql.transactions.transaction
//
//fun Application.configureDatabase() {
//    val url = environment.config.property("db.url").getString()
//    val user = environment.config.property("db.user").getString()
//    val password = environment.config.property("db.password").getString()
//    val driverClassName = environment.config.property("db.driver").getString()
//
//    Database.connect(
//        url = url,
//        driver = driverClassName,
//        user = user,
//        password = password
//    )
//
//    transaction {
//        AppUser.all()
//            .toList()
//    }
//}
//
//object AppUsers : IntIdTable(name = "app_user") {
//    val email = varchar("email", length = 255).uniqueIndex()
//}
//
//class AppUser(id: EntityID<Int>) : IntEntity(id) {
//    companion object : IntEntityClass<AppUser>(AppUsers)
//
//    var email by AppUsers.email
//}