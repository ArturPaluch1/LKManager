package LKManager.services.RedisService;

import LKManager.model.Table;
import LKManager.services.GsonService;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@Data
public class RedisTableService {
    private final GsonService gsonService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOperations;
    public RedisTableService(GsonService gsonService, RedisTemplate<String, Object> redisTemplate) {
        this.gsonService = gsonService;
        this.redisTemplate = redisTemplate;
        this.valueOperations = this.getRedisTemplate().opsForValue();;
    }

    public   Table getTable(String chosenscheduleName)
    {
        String tableJson  =(String) valueOperations.get("table:"+chosenscheduleName);
        if(tableJson!=null)
        {
            return gsonService.jsonToObject(tableJson,Table.class);
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
return this.getTable(table.getScheduleName());

    }


}
