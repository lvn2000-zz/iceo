package example

import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import example.actor.ActorSystemTest
//import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

trait TestInit {

  implicit val timeToWait = Duration(10, TimeUnit.SECONDS)
  implicit val timeout = Timeout(10, TimeUnit.SECONDS)

  implicit val systemTest = new ActorSystemTest()
  implicit val system = systemTest.system
  implicit val ex = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val testDB = "db.test"
  val config = ConfigFactory.parseResources("defaults.conf")
  implicit val dbSlick = Database.forConfig(path = testDB,  config = config) //forURL("jdbc:oracle:thin:@10.196.221.30:1521/casino.world", Map( "user" -> "kiosk1", "password" -> "u-+!LEGtQ516+7-Bhu,oKFZxhj(C%V"))

  //  val realCasinoFixture = CasinoFixture(
  //    Casino(Code = 48005, Name = "playtech48005", CasinoType = "Managed", ShortName = "4E")
  //  )
  //
  //  val rndCasinoFixture = CasinoFixture(Casino(Code = Random.nextInt(100000), Name = s"user_name_test_${Random.nextInt(100)}", CasinoType = "Managed", ShortName = "4S"))
  //
  //  val realEntityFixture = EntityFixture(Entity(Code = 648351,
  //    CasinoCode = realCasinoFixture.value.Code,
  //    Name = "GAMES_LIMITS",
  //    ParentCode = None,
  //    LNParentCode = None,
  //    IsEntityGamesConfigured = 1,
  //    IsEntityGamesLimitConfigured = 1,
  //    IsLiveNetworkRestricted = 0,
  //    Frozen = 0,
  //    JPParentCode = Some(648351),
  //    IsJackpotsConfigured = 1,
  //    IsURLConfigured = 1,
  //    ActiveLabel = None,
  //    EntityLabel = None,
  //    CompointButtonEnabled = None,
  //    GameMasterEnabled = None,
  //    EGParentCode = Some(648351),
  //    LMParentCode = Some(648351),
  //    UrlParentCode = Some("648351"),
  //    CompointButtonURL = None)
  //  )
  //
  //  val realKioskPlayeFixture = KioskPlayerFixture(KioskPlayer(Code = 9301,
  //    PlayerCode = 10298403,
  //    PlayerName = "GLTLEPL",
  //    CasinoName = realCasinoFixture.value.Name,
  //    EntityCode = realEntityFixture.value.Code)
  //  )
  //
  //  val rndKioskPlayeFixture = KioskPlayerFixture(KioskPlayer(Code = Random.nextInt(10000),
  //    PlayerCode = Random.nextInt(100000000),
  //    PlayerName = s"user_name_test_${Random.nextInt(100)}",
  //    CasinoName = "playtech48005",
  //    EntityCode = Random.nextInt(1000000))
  //  )
  //
  //  val realNameCasino = realCasinoFixture.value.Name
  //  val realNameUser = realKioskPlayeFixture.value.PlayerName //"MAK6RIQL.A6HI"
  //
  //  //  val realNameCasino = "playtech48005"
  //  //  val realNameUser = "MARTA1502"//"IRYNALEUSD10"//"CHILDPLAYERIRYNAEUR"//"IRYNALEUSD10"//"MARTA1502"
  //  val realTleId = realEntityFixture.value.Code
  //  val realParentTleId = realEntityFixture.value.ParentCode.getOrElse(0)
  //  val realDomain = "TLE"
  //
  //  val randomNameCasino = rndCasinoFixture.value.Name
  //  val randomNameUser = rndKioskPlayeFixture.value.PlayerName
  //  val randomTleId = Random.nextInt(100)
  //  val randomDomain = Random.nextInt(100).toString


}
