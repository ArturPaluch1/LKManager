package LKManager;

import LKManager.DAO.ScheduleDAO;
import LKManager.DAO.UserDAO;
import LKManager.LK.Comparators.ScheduleByLocalDateComparator;
import LKManager.LK.Schedule;
import LKManager.services.Cache.MZCache;
import LKManager.services.ScheduleService;
import LKManager.services.ScheduleServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.collection.internal.PersistentBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@SpringBootApplication
public class LKManagerApplication {
	@Autowired

	protected MZCache mzCache;
	protected final UserDAO userDAO;
	protected final ScheduleDAO scheduleDAO;
	private final ScheduleServiceImpl scheduleService;
	private static final long delay = 14L;

	public LKManagerApplication(MZCache mzCache, UserDAO userDAO, ScheduleDAO scheduleDAO, ScheduleServiceImpl scheduleService) {
		this.mzCache = mzCache;
		this.userDAO = userDAO;
		this.scheduleDAO = scheduleDAO;
		this.scheduleService = scheduleService;
	}


	protected void startTimer() {


		Timer timer = new Timer();

		// long delay = 1000L;


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


					//	url = new URL("https://www.developer.com/java/java-event-listeners/");
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


			}
		};


		//	timer.schedule(task,0,750000);
		// \/ 12,5 minut
		timer.schedule(task, 0, 750000);
		//timer.schedule(task, Date.from(now.plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant()) );
	}





	protected void startTimer2() {
		Timer timer = new Timer();
		TimerTask task2 = new TimerTask() {

			@Override
			public void run() {
				//	Queue<MZCacheAction> actionQuery = new LinkedList<>();

				if (mzCache.getSchedules().size() == 0 || mzCache.getUsers().size() == 0) {
					initializeUsersAndTheNewestSchedule();
				}
				else
				{
					MZCacheAction action=null;
					for (Schedule s : mzCache.getSchedules()
					) {
						if(!((PersistentBag) s.getRounds().get(0).getMatches()).wasInitialized())
						{
							System.out.println(s.getName() + "not initialized");
							action=new updateScheduleCacheByScheduleId(s.getId(), mzCache, scheduleService, scheduleDAO);
							break;
						}


					}
					if(action!=null)
					{
						action.update();
						System.out.println("updated ");
					}

			/*	for (var i : actionQuery
				) {
					i.update();
					System.out.println("updated ");
				}*/
				}


			}
		};
		//\/ 62 minuty  3720000
		//30000  30s
		//prod co 62 min
		timer.schedule(task2, 3720000, 3720000  );
		//dev co 30s
	//	timer.schedule(task2, 0 , 30000 );
	}
	public static void main(String[] args) {

		SpringApplication.run(LKManagerApplication.class, args);





	}


	@Bean
	ApplicationRunner applicationRunner(Environment environment) {

		return args -> {


			if (mzCache.getSchedules().size() == 0 || mzCache.getUsers().size() == 0)
	initializeUsersAndTheNewestSchedule();

			System.out.println(environment.getProperty("msg"));
			startTimer();
			startTimer2();


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


	public void initializeUsersAndTheNewestSchedule() {
		MZCacheAction userCacheUpdate = new UpdateUsersCache(mzCache, userDAO);
		userCacheUpdate.update();
		System.out.println("users cache updated");

//dodawanie schedules (bez info o rundach, meczach itd posortowane od najnowszego)
		MZCacheAction schedulesCacheUpdate = new updateSchedulesCache(mzCache, scheduleDAO);
		schedulesCacheUpdate.update();
		System.out.println("schedules cache updated");

//dodawanie kompletnych danych do najnowszego schedule
		MZCacheAction theNewestScheduleUpdate = new updateTheNewestScheduleCache(mzCache, scheduleService, scheduleDAO);
		theNewestScheduleUpdate.update();
		System.out.println("the newest cache updated");

	}


	abstract class MZCacheAction {


		public abstract void update();

	}

	@AllArgsConstructor
	@Getter
	@Setter
	class UpdateUsersCache extends MZCacheAction {
		@Autowired
		private MZCache mzCache;
		private final UserDAO userDAO;

		@Override @Transactional
		public void update() {
			//dodawanie users do cache
			mzCache.setUsers(userDAO.findNotDeletedUsers());
		}
	}

	@AllArgsConstructor
	@Getter
	@Setter
	class updateSchedulesCache extends MZCacheAction {
		@Autowired
		private MZCache mzCache;
		private final ScheduleDAO scheduleDAO;

		@Override @Transactional
		public void update() {
			List<Schedule> schedules = scheduleDAO.findAllFetchRoundsEagerly();
			schedules.sort(new ScheduleByLocalDateComparator());
			mzCache.setSchedules(schedules);

		}
	}

	@AllArgsConstructor
	@Getter
	@Setter
	class updateScheduleCacheByScheduleId extends MZCacheAction {

		private long id;

		@Autowired
		private MZCache mzCache;
		private final ScheduleService scheduleService;
		private final ScheduleDAO scheduleDAO;


		@Override @Transactional
		public void update() {
			List<Schedule> schedules = null;
			if (mzCache.getSchedules().size() != 0) {
				schedules = mzCache.getSchedules();
			} else {
				schedules = scheduleDAO.findAllFetchRoundsEagerly();
			}

			//List<Schedule> finalSchedules = schedules;
			 mzCache.getSchedules().stream().filter(s -> s.getId() == id).findFirst().get().setRounds(scheduleService.findByIdWithRoundsMatchesUsersAndTeams(id).getRounds());
			//System.out.println("schedule cache update"+temps.getRounds().get(0).getMatches());
		}
	}

	@AllArgsConstructor
	@Getter
	@Setter
	class updateTheNewestScheduleCache extends MZCacheAction {
		@Autowired
		private MZCache mzCache;
		private final ScheduleService scheduleService;
		private final ScheduleDAO scheduleDAO;

		@Override @Transactional
		public void update() {

			List<Schedule> schedules = null;
			if (mzCache.getSchedules().size() != 0) {
				schedules = mzCache.getSchedules();
			} else {
				schedules = scheduleDAO.findAllFetchRoundsEagerly();
			}

			List<Schedule> finalSchedules = schedules;
			var temps = mzCache.getSchedules().stream().filter(s -> s.getId() == finalSchedules.get(0).getId()).findFirst().get();
			temps.setRounds(scheduleService.findByIdWithRoundsMatchesUsersAndTeams(schedules.get(0).getId()).getRounds());

		}
	}




}
