package LKManager;

import LKManager.DAO_SQL.ScheduleDAO;
import LKManager.DAO_SQL.UserDAO;
import LKManager.TimerTasks.RefreshSiteTimer;
import LKManager.TimerTasks.RoundResultsUpdateTimer;
import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.services.RedisService.RedisUserService;
import LKManager.services.ResultsService;
import LKManager.services.ScheduleService;
import LKManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;


@SpringBootApplication
public class LKManagerApplication {


	@Autowired

//	private final CacheService cacheService;
//	protected MZCache mzCache;
	protected final UserDAO userDAO;
	protected final UserService userService;
	private final ResultsService resultsService;
	protected final ScheduleDAO scheduleDAO;
	private final ScheduleService scheduleService;

	private final RedisUserService redisUserService;
	/*@Autowired
	private FailedDatabaseOperationRepository failedDatabaseOperationRepository;*/
	private static final long delay = 14L;

	public LKManagerApplication( UserDAO userDAO, UserService userService, ResultsService resultsService, ScheduleDAO scheduleDAO, ScheduleService scheduleService, RedisUserService redisUserService) {
	//	this.cacheService = cacheService;
	//	this.mzCache = mzCache;
		this.userDAO = userDAO;
		this.userService = userService;
		this.resultsService = resultsService;
		this.scheduleDAO = scheduleDAO;
		this.scheduleService = scheduleService;
		this.redisUserService = redisUserService;
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
		RefreshSiteTimer refreshSiteTimer= new RefreshSiteTimer();
		refreshSiteTimer.schedule(refreshSiteTimer.getTask(),0,750000 );//by~12 minutes

		RoundResultsUpdateTimer roundResultsUpdateTimer = new RoundResultsUpdateTimer(resultsService);
		roundResultsUpdateTimer.schedule(roundResultsUpdateTimer.getTask(),0, 60 * 60 * 1000);//by a hour
		//	timer.schedule(task,0,750000);
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
	ApplicationRunner applicationRunner(Environment environment) {

		return args -> {
			List<UserDataDTO> usersNotDeletedWithPause=	userService.findAllUsers(false,true);//findUsers_NotDeletedWithPause();
			List<UserDataDTO> usersNotDeletedWithoutPause=	userService.findAllUsers(false,false);//findUsers_NotDeletedWithoutPause();

			List<UserDataDTO> usersDeletedWithPause=	userService.findAllUsers(true,true);//findUsers_NotDeletedWithPause();
			List<UserDataDTO> usersDeletedWithoutPause=	userService.findAllUsers(true,false);



			System.out.println(	"\nusersNotDeletedWithPause\n"+ usersNotDeletedWithPause);
			System.out.println("\n usersNotDeletedWithoutPause\n"	+ usersNotDeletedWithoutPause);
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
