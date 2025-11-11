package io.github.alelk.pws.server.transport.routes


import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.*
import io.github.alelk.pws.api.contract.book.Books
import io.github.alelk.pws.api.mapping.book.toDomain
import io.github.alelk.pws.api.mapping.book.toDto
import io.github.alelk.pws.api.mapping.core.toDomain
import io.github.alelk.pws.domain.book.query.BookQuery
import io.github.alelk.pws.domain.book.query.BookSort
import io.github.alelk.pws.domain.book.repository.BookRepository
import io.github.alelk.pws.domain.core.ids.BookId
//import io.ktor.server.application.Application
//import io.ktor.server.resources.get
//import io.ktor.server.response.respond
//import io.ktor.server.routing.routing
import kotlinx.coroutines.flow.first



fun Application.bookRoutes(bookRepository: BookRepository) {
  routing {
    get<Books> { res ->
      val query = BookQuery(locale = res.locale?.toDomain(), enabled = res.enabled, minPriority = res.minPriority)
      val sort = res.sort?.toDomain() ?: BookSort.ByPriorityDesc
      val summaries = bookRepository.observeBooks(query, sort).first()
      call.respond(summaries.map { it.toDto() })
    }
    get<Books.ById> { res ->
      val detail = bookRepository.get(BookId.parse(res.id.value))
      if (detail == null) call.respond(status = io.ktor.http.HttpStatusCode.NotFound, mapOf("error" to "Book not found"))
      else call.respond(detail.toDto())
    }
    get<Books.ById.Songs> { res ->
      // TODO implement songs list inside book when repository supports
      call.respond(emptyMap<Int, Any>())
    }
  }
}
