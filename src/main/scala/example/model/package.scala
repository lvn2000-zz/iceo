package example

import spray.json.DefaultJsonProtocol.jsonFormat2


package object model {

  sealed trait Message

  case class PaginatedResult[T](
                                 totalCount: Int,
                                 entities: List[T],
                                 hasNextPage: Boolean
                               ) extends Message

  case class User(id: Long, email: String) extends Message

  case class Post(id: Long, user_id: Long, title: String) extends Message

  case class Comment(id: Long, user_id: Long, post_id: Long, body: String) extends Message




}
