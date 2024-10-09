package LKManager;

import LKManager.DAO_SQL.RoundDAO;
import LKManager.DAO_SQL.ScheduleDAO;
import LKManager.DAO_SQL.UserDAO;
import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.UserMZ.LeagueParticipation;
import LKManager.model.UserMZ.MZUserData;
import LKManager.model.UserMZ.Team;
import LKManager.model.account.Role;
import LKManager.model.account.User;
import LKManager.services.RedisService.RedisScheduleService;
import LKManager.services.RedisService.RedisTableService;
import LKManager.services.RedisService.RedisUserService;
import LKManager.services.*;
import LKManager.services.Scheduling.LeaguePlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
@SpringBootApplication
public class LKManagerApplication {


	@Autowired

//	private final CacheService cacheService;
//	protected MZCache mzCache;
	protected final UserDAO UserDAO;
	protected final UserService userService;
	private final ResultsService resultsService;
	protected final ScheduleDAO scheduleDAO;
	private final ScheduleService scheduleService;
	private final RedisTableService redisTableService;
	private final RedisScheduleService redisScheduleService;
	private final TableService tableService;
private final RoundService roundService;
private final LeaguePlanningService leaguePlanningService;

private final UserDAO userDAO;
	private final RedisUserService redisUserService;
	/*@Autowired
	private FailedDatabaseOperationRepository failedDatabaseOperationRepository;*/
	private static final long delay = 14L;
private final RoundDAO roundDAO;
	public LKManagerApplication(UserDAO UserDAO, UserService userService, ResultsService resultsService, ScheduleDAO scheduleDAO, ScheduleService scheduleService, RedisTableService redisTableService, RedisScheduleService redisScheduleService, TableService tableService, RoundService roundService, LeaguePlanningService leaguePlanningService, LKManager.DAO_SQL.UserDAO userDAO, RedisUserService redisUserService, RoundDAO roundDAO) {
	//	this.cacheService = cacheService;
	//	this.mzCache = mzCache;
		this.UserDAO = UserDAO;
		this.userService = userService;
		this.resultsService = resultsService;
		this.scheduleDAO = scheduleDAO;
		this.scheduleService = scheduleService;
		this.redisTableService = redisTableService;
		this.redisScheduleService = redisScheduleService;
		this.tableService = tableService;
		this.roundService = roundService;
		this.leaguePlanningService = leaguePlanningService;
		this.userDAO = userDAO;
		this.redisUserService = redisUserService;
		this.roundDAO = roundDAO;
	}


	protected void startTimers() {


	//	Timer timer = new Timer();

		// long delay = 1000L;

/*
		TimerTask task = new TimerTask() {
			int i = -2147483000;

			public void run() {
				System.out.println("Task performed on: " + new Date() + "n" +
						"Thread's name: " + Thread.currentThread().getName());
				if (i == 2147483000)
					i = -2147483000;
				i++;
				URL url = null;
				try {
					url = new URL("https://lkm-fgim.onrender.com/");



				} catch (MalformedURLException e) {
					//	throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				URLConnection urlConnection = null;
				try {

					urlConnection = url.openConnection();
					urlConnection.connect();


					//	System.out.println("content= "+	urlConnection.getContent()+"\n");

					BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
							StandardCharsets.UTF_8));


					System.out.println("i=" + i + "refreshed site ");
				} catch (IOException e) {
					//	throw new RuntimeException(e);
					System.out.println("refresh site failed ");
				}

			/*	Object c= null;
				try {
					c = urlConnection.getContent();
				} catch (IOException e) {
				//	throw new RuntimeException(e);
				}*/

/*
			}
		};
*/
	/*	RefreshSiteTimer refreshSiteTimer= new RefreshSiteTimer();
		refreshSiteTimer.schedule(refreshSiteTimer.getTask(),0,750000 );//by~12 minutes
*/
	//	RoundResultsUpdateTimer roundResultsUpdateTimer = new RoundResultsUpdateTimer(resultsService);
	//	roundResultsUpdateTimer.schedule(roundResultsUpdateTimer.getTask(),0, 60 * 60 * 1000);//by a hour
	//	roundResultsUpdateTimer.schedule(roundResultsUpdateTimer.getTask(),0, 60 *1000);

		/*RoundMatchesUpdateTimer roundMatchesUpdateTimer= new RoundMatchesUpdateTimer(scheduleService,roundService,redisTableService,tableService,redisScheduleService);
		roundMatchesUpdateTimer.schedule(roundMatchesUpdateTimer.getTask(),0,60 * 60 * 1000);
	*/	//	timer.schedule(task,0,750000);
		// \/ 12,5 minut
	//	timer.schedule(task, 0, 750000);
		//timer.schedule(task, Date.from(now.plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant()) );
	}





	/*protected void startTimer2() {
		Timer timer = new Timer();

		//\/ 62 minuty  3720000
		//30000  30s
		//prod co 62 min
	//	timer.schedule(task2, 3720000, 3720000  );
		//dev co 30s
		timer.schedule(task2, 30000 , 30000 );
	}
*/



	public static void main(String[] args) {


			SpringApplication.run(LKManagerApplication.class, args);








	}


	@Bean
	ApplicationRunner applicationRunner(Environment environment) throws NoSuchAlgorithmException {




		return args -> {
			List<UserDataDTO> usersActiveWithPause=	userService.findAllMZUsers(true,true);//findUsers_NotDeletedWithPause();
			List<UserDataDTO> usersActiveWithoutPause=	userService.findAllMZUsers(true,false);//findUsers_NotDeletedWithoutPause();

		//	List<UserDataDTO> usersDeactivatedWithPause=	userService.findAllUsers(false,true);//findUsers_NotDeletedWithPause();
		//	List<UserDataDTO> usersDeactivatedWithoutPause=	userService.findAllUsers(false,false);



			System.out.println(	"+++\nusersNotDeletedWithPause\n"+ usersActiveWithPause);
			System.out.println("---\n usersNotDeletedWithoutPause\n"	+ usersActiveWithoutPause);


if(userService.getUserByUsername("pauza")==null) {

	System.out.println("Building pause object");
	createPauseObject();
}


/*
			List<Schedule> schedules1 = scheduleService.getSchedules().stream().filter(s -> s.getName().contains("Liga")).collect(Collectors.toList());
		//nie ma trwającej ligi
		if(schedules1.stream().filter(s->s.getScheduleStatus().equals(ScheduleStatus.ONGOING)).collect(Collectors.toList()).size()==0)
		{
			List<Schedule> plannedSchedules=	schedules1.stream().filter(s->s.getScheduleStatus().equals(ScheduleStatus.PLANNED)).collect(Collectors.toList());
				//jest zaplanowany schedule
			if(plannedSchedules.size()!=0)
			{
			Schedule scheduleToChange=	scheduleDAO.findByIdAndFetchMatchesEagerly(plannedSchedules.get(0).getId());
				scheduleToChange.setScheduleStatus(ScheduleStatus.ONGOING);
scheduleService.updateSchedule(scheduleToChange);


			}
			else //nie ma zaplanowanego, planowanie
			{
//todo z metody w serwisie
			}

		}*/
		/*	List<Round> rounds = roundDAO.findLastRoundOFSchedulesOfGivenType(ScheduleType.standardSchedule);
			List<Round> leagueRounds = rounds.stream().filter(round -> round.getSchedule().getName().contains("Liga")).collect(Collectors.toList());
			Optional<Match> match1=leagueRounds.get(0).getMatches()
					.stream().filter(match ->(
							(match.getOpponentMatchResult1()!=null && match.getUserMatchResult1()!=null)
									||  (match.getOpponentMatchResult2()!=null && match.getUserMatchResult2()!=null))).findFirst();



*/



/*

	List<Schedule>		leagueSchedules=	scheduleService.getSchedules().stream().filter(s->s.getName().contains("Liga")).collect(Collectors.toList());

		//if(	scheduleService.getSchedules().stream().filter(s->(s.getName().contains("Liga")&&s.getScheduleStatus().equals(ScheduleStatus.PLANNED))).collect(Collectors.toList()).size()==0)

			if(leagueSchedules.isEmpty()) //nie ma żadnych ligowych
			{
				LocalDate today = LocalDate.now();
				LocalDate nextTuesday = today.with(DayOfWeek.TUESDAY);

				// Jeśli dzisiaj jest już wtorek, to dodajemy 7 dni, aby uzyskać najbliższy przyszły wtorek
				if (today.getDayOfWeek() == DayOfWeek.TUESDAY) {
					nextTuesday = nextTuesday.plusDays(7);
				}
				scheduleService.planSchedule(nextTuesday, "Liga s.1", ScheduleType.standardSchedule);

			}
			else
			{
				//nie ma planned
				if(leagueSchedules.stream().filter(s->s.getScheduleStatus().equals(ScheduleStatus.PLANNED)).findFirst().isEmpty()) {



						//	int leagueSeasonNumber= extractNumber(leagueRounds.get(0).getSchedule().getName());
				Optional<Schedule> lastSchedule=	leagueSchedules.stream().max(Comparator.comparing(Schedule::getEndDate)).stream().findAny();

						scheduleService.planSchedule(lastSchedule.get().getEndDate().plusDays(7), "Liga s." + (LKManagerApplication.extractNumber(lastSchedule.get().getName())+1), ScheduleType.standardSchedule);

			}
				else  //jest już planned
				{

				}
			}*/



//		leaguePlanningService.manageLeague();
		//c++;



/*
leaguePlanningService.planUpcomingLeagueSchedule();
			leaguePlanningService.manageLeague();

*/



	/*	List<Round>rounds=		roundDAO.findLastRoundOFSchedulesOfGivenType(ScheduleType.standardSchedule);

List<Schedule> schedulesNames=scheduleService.getSchedules();

				schedulesNames=schedulesNames.stream().filter(s -> s.getName().contains("Liga")).collect(Collectors.toList());
				OptionalInt maxNumber = schedulesNames.stream()
						.mapToInt(schedule -> extractNumber(schedule.getName())) // Zamiana na strumień liczb
						.max();



				List<Round>leagueRounds=	rounds.stream().filter(round -> round.getSchedule().getName().contains("Liga")).collect(Collectors.toList());

		if(schedulesNames.size()==0)//nie było wcześniej schedule z ligą
	*/


			/*	List<Schedule> finishedLeagueSchedules = scheduleService.getSchedules().stream().filter(s -> (s.getName().contains("o") && s.getScheduleStatus().equals(ScheduleStatus.ONGOING))).collect(Collectors.toList());
				int i = 0;
				finishedLeagueSchedules.forEach(schedule -> Hibernate.initialize(schedule.getRounds()));
				Optional<Schedule> latestSchedule = finishedLeagueSchedules.stream()
						.filter(schedule -> !schedule.getRounds().isEmpty()) // Upewniamy się, że są rundy
						.max(Comparator.comparing(schedule ->
								schedule.getRounds().stream()
										.map(Round::getDate) // Pobieramy daty startu
										.max(Comparator.naturalOrder()) // Znajdujemy najnowszą datę w każdej rundzie
										.orElse(LocalDate.MIN) // Jeśli brak dat, ustawiamy minimalną możliwą datę
						));

				i = 0;*/








		//	UserDataDTO userDataDTO=userService.addUser(new SignUpForm("pauza","pauza@pauza","admin123"));


			/*
			List<UserData> users=	userService.findUsers_NotDeletedWithPause();

			List<UserDataDTO> usersDTO = new ArrayList<>();

			for (UserData user:users
			) {
				usersDTO.add(new UserDataDTO().UserDataToDTO(user));
			}
			Gson gson = new Gson();
			String usersJson = gson.toJson(usersDTO);

			ValueOperations<String, Object> valueOperations =	redisService.getRedisTemplate().opsForValue();
			valueOperations.set("users1",usersJson);
*/


		/** to jest niepotrzebne chyba bo w user service sie dodaje
		*
			redisService.addAllUsers(usersNotDeletedWithPause,false,true);
			redisService.addAllUsers(usersNotDeletedWithoutPause);
		*
			*/


		/*	Gson gson = new Gson();

			ValueOperations<String, Object> valueOperations =	redisService.getRedisTemplate().opsForValue();
			String  r= valueOperations.get("users1").toString();
			List<UserDataDTO> usersFromRedis = gson.fromJson(r, new TypeToken<List<UserDataDTO>>(){}.getType());
*/
		//	List<UserDataDTO> usersFromRedis = redisService.GetUsers_NotDeletedWithPause();
		//	System.out.println(	usersFromRedis);
	/*
		List<UserData> users=	userService.findUsers_NotDeletedWithPause();
			List<UserDataDTO> usersDTO = new ArrayList<>();

			for (UserData user:users
				 ) {
				usersDTO.add(new UserDataDTO().UserDataToDTO(user));
			}




// Konwertowanie listy na JSON
			Gson gson = new Gson();
			String usersJson = gson.toJson(usersDTO);//.subList(0,2));

			// Konfiguracja klienta Redisson
			Config config = new Config();
			config.useSingleServer().setAddress("redis://127.0.0.1:6379");
			// Tworzenie instancji Redis Object Mapper
			RedissonClient redisson = Redisson.create(config);




			// Zapisanie JSON do Redis
			redisson.getBucket("users").set(usersJson);

			// Pobranie JSON z Redis
			String usersJsonFromRedis = (String) redisson.getBucket("users").get();

			// Konwertowanie JSON z powrotem na listę użytkowników
			List<UserDataDTO> usersFromRedis = gson.fromJson(usersJsonFromRedis, new TypeToken<List<UserDataDTO>>(){}.getType());

			// Zamknięcie klienta Redisson
			redisson.shutdown();


*/



	/*
			if (mzCache.getSchedules().size() == 0 || mzCache.getUsers().size() == 0)
	cacheService.initializeUsersAndTheNewestSchedule();
*/




			System.out.println(environment.getProperty("msg"));


	//	cacheService.	initializeCache();

			startTimers();
			//startTimer2();



	/*

			List<Schedule> schedules= scheduleDAO.findAll();
			schedules.forEach(s->
					s.setRounds(		scheduleService.getByIdWithRoundsMatchesUsersAndTeams(s.getId()).getRounds())
					);

mzCache.setSchedules(schedules);
mzCache.getSchedules().get(0).getRounds().forEach(r->r.getMatches().forEach(m-> System.out.println(m.getUserData().getUsername()+" vs "+m.getOpponentUserData().getUsername()+"\br")));
*/






		};


	}

	private static int extractNumber(String scheduleName) {
		return Integer.parseInt(scheduleName.substring(7).trim());
	}

	private void createPauseObject() {
		MZUserData tempMZUser = new MZUserData();
		Team tempTeam = new Team();
		tempTeam.setTeamName(" ");
		tempTeam.setTeamId(0);
		tempMZUser.setTeamlist(Collections.singletonList(tempTeam));
		tempMZUser.setUsername("pauza");
		tempMZUser.setMZuser_id(0L);


		User newPauseUser = new User();

		newPauseUser.setUsername("pauza");


		tempTeam.setUser(tempMZUser);

		newPauseUser.setMzUser(tempMZUser);
		newPauseUser.setReliability(-9999);
		newPauseUser.setLeagueParticipation(LeagueParticipation.UNSIGNED);
		newPauseUser.setRole(Role.ACTIVATED_CLUB_USER);
		newPauseUser.setEmail("pauza@pauza");
		newPauseUser.setPassword("");

		User addedUser = userDAO.saveUser(newPauseUser);
		User user1 = userDAO.findUserByName(addedUser.getUsername());


		//redisUserService.addUserToRedis(user1);
		redisUserService.saveOrUpdateUserInUserLists(user1);
		System.out.println("Saved pause object in Redis");
	}


	@PostConstruct
	public void init() {
		System.out.println("Initialized...");


//AppConfig appConfig = new AppConfig();
//appConfig.myService();


	}

/*

	@Configuration
	@RequiredArgsConstructor

	public class AppConfig {


@Bean
		public MZCache myService(ScheduleDAO scheduleDAO) {

	MZCache mzCache = new MZCache(scheduleDAO);
			mzCache.setTable( new Table());
			mzCache.setUsers( new ArrayList<>());
			mzCache.setSchedules( new ArrayList<>());
			mzCache.setMatches(new ArrayList<>());
			return mzCache;
		}
	}
*/

	@PreDestroy
	public void destroy() {
		System.out.println("Destroying...");
	}

	/*
properties np dla baz danych
https://www.udemy.com/course/spring-framework-5-beginner-to-guru/learn/lecture/25220804#content
https://www.udemy.com/course/spring-framework-5-beginner-to-guru/learn/lecture/25220792#content


	 */

















}
