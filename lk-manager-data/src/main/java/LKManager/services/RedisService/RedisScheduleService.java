package LKManager.services.RedisService;

import LKManager.DAO_SQL.ScheduleDAO;
import LKManager.model.RecordsAndDTO.ScheduleDTO;
import LKManager.model.RecordsAndDTO.ScheduleNameDTO;
import LKManager.services.GsonService;
import lombok.Data;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class RedisScheduleService {
    private final GsonService gsonService;
    private final ValueOperations<String, Object> valueOperations;
    private final ListOperations<String, String> listOperations;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, String> redisListTemplate;
private final ScheduleDAO scheduleDAO;
    public RedisScheduleService(GsonService gsonService, RedisTemplate<String, Object> redisTemplate, RedisTemplate<String, String> redisListTemplate, ScheduleDAO scheduleDAO) {
        this.gsonService = gsonService;
        this.redisTemplate = redisTemplate;
        this.redisListTemplate = redisListTemplate;
        this.scheduleDAO = scheduleDAO;


        this.valueOperations = this.getRedisTemplate().opsForValue();
        this.listOperations = this.getRedisListTemplate().opsForList();
    }

    public List<ScheduleNameDTO> getScheduleNames()
    {

        List<String> jsonList = listOperations.range("scheduleNames", 0, -1);
       /* List<ScheduleNamesDTO> scheduleNames = new ArrayList<>();
        for (String json : jsonList) {
                 ScheduleNamesDTO scheduleNamesDTO = gsonService.jsonToObject(json, ScheduleNamesDTO.class);
            if (scheduleNamesDTO != null) {
                scheduleNames.add(scheduleNamesDTO);
            }
        }
        return scheduleNames;*/


        return gsonService.jsonToList(jsonList, ScheduleNameDTO.class);
    }
    public Long setScheduleNames(List<ScheduleNameDTO> scheduleNames)
    {
        redisTemplate.delete("scheduleNames");
    //  String json=  gsonService.listToJson(scheduleNames);
        List<String> jsonList = gsonService.listToJson(scheduleNames);
             /*   = scheduleNames.stream()
                .map(schedule -> gsonService.objectToJson(schedule))
                .collect(Collectors.toList());*/
       return listOperations.leftPushAll("scheduleNames",jsonList);
    }
    public ScheduleDTO getSchedule_ByName(String scheduleName)
    {
 //       scheduleName.replace(" ","_");

        Optional<ScheduleNameDTO> scheduleNameDTO= this.getScheduleNames().stream().filter(schedule-> schedule.getName().equals(scheduleName)).findFirst();

  //     String scheduleJson=(String) valueOperations.get("schedule:"+scheduleName.replace(" ","_"));
        String scheduleJson=null;
        if(scheduleNameDTO.isPresent())
        {
            scheduleJson  =(String) valueOperations.get("schedule:"+scheduleNameDTO.get().getId());
        }

       if(scheduleJson!=null)
       {
           System.out.println("found schedule by name in redis");
           return gsonService.jsonToObject(scheduleJson,ScheduleDTO.class);
       }
   else{
           System.out.println("did not found schedule by name in redis");
           return null;
       }

    }

    public ScheduleDTO setSchedule(ScheduleDTO schedule)
    {
    /*    String test2=  gsonService.objectToJson((schedule.getRounds().get(0)).getMatchesDTO().get(0));
      String test1=  gsonService.objectToJson((schedule.getRounds().get(0)));*/

//schedule.setName(schedule.getName().replace(" ","_"));

      String scheduleJson=  gsonService.objectToJson(schedule);
//String scheduleName=schedule.getName().replace(" ","_");

        valueOperations.set("schedule:"+schedule.getId(), scheduleJson);
        if(scheduleJson!=null)
        {

           List<ScheduleNameDTO>scheduleNames= this.getScheduleNames();
           if(scheduleNames.isEmpty())
           {

               scheduleNames=scheduleDAO.getScheduleNames();

           }
           if(scheduleNames==null)
           {
               return null;
           }
           else
           {
               if(!scheduleNames.contains(new ScheduleNameDTO(schedule.getId(),schedule.getName())))
               {
                   scheduleNames.add(new ScheduleNameDTO(schedule.getId(),schedule.getName()));
                   this.setScheduleNames(scheduleNames);
               }

              // return this.getSchedule_ByName(scheduleName);
               return this.getSchedule_ById(schedule.getId());
           }




        }

        else
            return null;
    }

    ScheduleDTO getSchedule_TheNewest()
    {
        //todo
        return null;
    }


    public boolean deleteScheduleByName(ScheduleNameDTO scheduleNameDTO) {
try {

//Schedule schedule=scheduleDAO.findByScheduleName(scheduleToDeleteName);

    redisTemplate.delete("schedule:"+scheduleNameDTO.getId());//.replace(" ", "_"));
    List<ScheduleNameDTO> scheduleNames = this.getScheduleNames();
    scheduleNames.remove(scheduleNameDTO);//.replace(" ", "_"));
    this.setScheduleNames(scheduleNames);
    return true;
}
catch(Exception e)
{
    return false;
}

    }

    public ScheduleDTO getSchedule_ById(long id) {
        String scheduleJson=(String) valueOperations.get("schedule:"+id);
        if(scheduleJson!=null)
        {
            System.out.println("found schedule by name in redis");
            return gsonService.jsonToObject(scheduleJson,ScheduleDTO.class);
        }
        else{
            System.out.println("did not found schedule by name in redis");
            return null;
        }


    }


}
