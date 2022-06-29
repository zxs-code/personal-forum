-- java.lang.Integer
if not redis.call('exists', KEYS[1]) then
    return -1;
end
local oldState = redis.call('hget', KEYS[1], ARGV[1])
local newState = ARGV[2];
if oldState == newState then
    return oldState;
end
redis.call('hset', KEYS[1], ARGV[1], newState);
--if newState == '0' then
--    redis.call('incr', KEYS[2]);
--elseif newState == '2' then
--    redis.call('incr', KEYS[3])
--end
--if oldState == '0' then
--    redis.call('decr', KEYS[2]);
--elseif oldState == '2' then
--    redis.call('decr', KEYS[3])
--end
return oldState;