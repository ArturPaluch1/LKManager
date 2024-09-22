package LKManager.services.RedisService;

import LKManager.model.Table;
import LKManager.services.GsonService;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Data
public class RedisTableService {
    private final GsonService gsonService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOperations;
    public RedisTableService(GsonService gsonService, RedisTemplate<String, Object> redisTemplate) {
        this.gsonService = gsonService;
        this.redisTemplate = redisTemplate;
        this.valueOperations = this.getRedisTemplate().opsForValue();
    }

    public   Table getTable(String chosenscheduleName)
    {
        String tableJson  =(String) valueOperations.get("table:"+chosenscheduleName);
        if(tableJson!=null)
        {
            redisTemplate.expire("table:"+chosenscheduleName,8, TimeUnit.DAYS);
           Table table=  gsonService.jsonToObject(tableJson,Table.class);
if(table.getPlayerSummaries().isEmpty())
            return null;
else return table;
        }
        else return null;

    }

   public Table setTable( Table table)
    {
Table tableAlreadyExist=getTable(table.getScheduleName());
if(tableAlreadyExist!=null) {
    redisTemplate.delete("table:" + table.getScheduleName());
}
        String jsonString=  gsonService.objectToJson(table);
valueOperations.set("table:"+ table.getScheduleName(),jsonString);
        redisTemplate.expire("table:"+ table.getScheduleName(),8, TimeUnit.DAYS);
return this.getTable(table.getScheduleName());

    }


}
