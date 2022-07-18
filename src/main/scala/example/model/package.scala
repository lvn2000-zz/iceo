package example


package object model {


  sealed trait Model

  case class PaginatedResult[T](
                                 totalCount: Int,
                                 entities: List[T],
                                 hasNextPage: Boolean
                               ) extends Model

  case class User(id: Long, email: String) extends Model

  case class Post(id: Long, user_id: Long, title: String) extends Model

  case class Comment(id: Long, user_id: Long, post_id: Long, body: String) extends Model


  sealed trait DMManager

  case class Update(newModel: Model) extends DMManager

  case class Insert(newModel: Model) extends DMManager

  case class Delete(id: Long, className: String) extends DMManager // className should be value [Model].getClass.getName

  case class FindOne(id: Long, className: String) extends DMManager // className should be value [Model].getClass.getName

  case class ResultDBManager(countRecord: Option[Int] = None, model: Option[Model] = None, message: Option[String] = None) extends DMManager


}
