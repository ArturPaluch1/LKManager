package LKManager.HardCodedCache_unused;

import LKManager.DAO.ScheduleDAO;
import LKManager.HardCodedCache_unused.Cache.MZCache;
import LKManager.model.Round;
import LKManager.model.Schedule;
import LKManager.services.ResultsService;
import LKManager.services.ScheduleService;
import LKManager.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.collection.internal.PersistentBag;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CacheService {

  private   MZCache mzCache;
    private final   ScheduleService scheduleService;
    private final   ScheduleDAO scheduleDAO;
    private final UserService userService;
    private final ResultsService resultsService;

    public void initializeCache() {

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
            else  //sprawdza dopiero jak wszystkie schedule sa w cache
            {
                checkRoundsToUpdate();
            }

		/*	for (var i : actionQuery
			) {
				i.update();
				System.out.println("updated ");
			}*/
        }
    }



    public void initializeUsersAndTheNewestSchedule() {
        MZCacheAction userCacheUpdate = new UpdateUsersCache(mzCache, userService);
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








    private void checkRoundsToUpdate() {
        List<Round> rounds=new ArrayList<>();



        List<Schedule> t = mzCache.getSchedules()
                .stream()
                .filter(s -> s.getRounds().stream()
                        .anyMatch(r -> r.getDate().equals(LocalDate.now().minusDays(1))))
                .collect(Collectors.toList());



        List<RoundAndSchedule> matchingRounds = mzCache.getSchedules()
                .stream()
                .flatMap(s -> s.getRounds().stream()
                        .filter(r -> r.getDate().equals(LocalDate.now().minusDays(1)))
                        .findFirst()
                        .map(round -> new RoundAndSchedule(round, s)) // Tworzy obiekt RoundAndSchedule
                        .stream())
                .collect(Collectors.toList());

        if(matchingRounds.size()!=0)
        {


            for (RoundAndSchedule ras:matchingRounds
            ) {
                if(ras.getRound().getMatches().stream().filter(
                                m->  (m.getUserMatchResult1()==null&&m.getOpponentMatchResult1()==null)||
                                        (m.getUserMatchResult2()==null&&m.getOpponentMatchResult2()==null)
                        )
                        .count()>=ras.getRound().getMatches().size())
                    try {

                        System.out.println("proba aktualizacji");
                        if(resultsService.updateResults(Long.valueOf(ras.getRound().getNr()).intValue(),ras.getSchedule())!=null)
                            System.out.println("proba udana");
                    } catch (DatatypeConfigurationException e) {
                        throw new RuntimeException(e);
                    } catch (ParserConfigurationException e) {
                        throw new RuntimeException(e);
                    } catch (JAXBException e) {
                        throw new RuntimeException(e);
                    } catch (SAXException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }

        }

    }


    @AllArgsConstructor @Getter
    @Setter
    public class RoundAndSchedule
    {
        private 	Round round;
        private 	Schedule schedule;
    }
}
