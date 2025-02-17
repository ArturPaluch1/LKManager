package LKManager.services.RedisService;

import LKManager.DAO_SQL.ScheduleDAO;
import LKManager.model.RecordsAndDTO.ScheduleDTO;
import LKManager.model.RecordsAndDTO.ScheduleSettingsDTO;
import LKManager.services.GsonService;
import lombok.Data;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    public List<ScheduleSettingsDTO> getScheduleNames()
    {

        List<String> jsonList = listOperations.range("scheduleNames", 0, -1);
        redisTemplate.expire("scheduleNames",8, TimeUnit.DAYS);
       /* List<ScheduleNamesDTO> scheduleNames = new ArrayList<>();
        for (String json : jsonList) {
                 ScheduleNamesDTO scheduleNamesDTO = gsonService.jsonToObject(json, ScheduleNamesDTO.class);
            if (scheduleNamesDTO != null) {
                scheduleNames.add(scheduleNamesDTO);
            }
        }
        return scheduleNames;*/


        return gsonService.jsonToList(jsonList, ScheduleSettingsDTO.class);
    }
    public Long setScheduleNames(List<ScheduleSettingsDTO> scheduleNames)
    {
        redisTemplate.delete("scheduleNames");
    //  String json=  gsonService.listToJson(scheduleNames);
        List<String> jsonList = gsonService.listToJson(scheduleNames);
             /*   = scheduleNames.stream()
                .map(schedule -> gsonService.objectToJson(schedule))
                .collect(Collectors.toList());*/
      Long getScheduleNamesResult=  listOperations.leftPushAll("scheduleNames",jsonList);
        redisTemplate.expire("scheduleNames",8, TimeUnit.DAYS);
        return getScheduleNamesResult;
    }
    public ScheduleDTO getSchedule_ByName(String scheduleName)
    {
 //       scheduleName.replace(" ","_");

        Optional<ScheduleSettingsDTO> scheduleNameDTO= this.getScheduleNames().stream().filter(schedule-> schedule.getName().equals(scheduleName)).findFirst();

  //     String scheduleJson=(String) valueOperations.get("schedule:"+scheduleName.replace(" ","_"));
        String scheduleJson=null;
        if(scheduleNameDTO.isPresent())
        {
            scheduleJson  =(String) valueOperations.get("schedule:"+scheduleNameDTO.get().getId());
            }

       if(scheduleJson!=null)
       {
           System.out.println("found schedule by name in redis");
           redisTemplate.expire("schedule:"+scheduleNameDTO.get().getId(),8, TimeUnit.DAYS);

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
        redisTemplate.expire("schedule:"+schedule.getId(),8, TimeUnit.DAYS);

        if(scheduleJson!=null)
        {

           List<ScheduleSettingsDTO>scheduleNames= this.getScheduleNames();
           if(scheduleNames.isEmpty())
           {

               scheduleNames=scheduleDAO.getScheduleNamesOngoingOrFinished();

           }
           if(scheduleNames==null)
           {
               return null;
           }
           else
           {
               if(!scheduleNames.contains(new ScheduleSettingsDTO(schedule.getId(),schedule.getName())))
               {
                   scheduleNames.add(new ScheduleSettingsDTO(schedule.getId(),schedule.getName()));
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


    public boolean deleteScheduleByName(ScheduleSettingsDTO scheduleSettingsDTO) {
try {

//Schedule schedule=scheduleDAO.findByScheduleName(scheduleToDeleteName);

    redisTemplate.delete("schedule:"+ scheduleSettingsDTO.getId());//.replace(" ", "_"));
    List<ScheduleSettingsDTO> scheduleNames = this.getScheduleNames();
    scheduleNames.remove(scheduleSettingsDTO);//.replace(" ", "_"));
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
            redisTemplate.expire("schedule:"+id,8, TimeUnit.DAYS);
            System.out.println("found schedule by name in redis");
            return gsonService.jsonToObject(scheduleJson,ScheduleDTO.class);
        }
        else{
            System.out.println("did not found schedule by name in redis");
            return null;
        }


    }


}
