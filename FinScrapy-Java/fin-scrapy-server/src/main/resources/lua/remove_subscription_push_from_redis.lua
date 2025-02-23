local key = KEYS[1]
local userId = ARGV[1]
local currentCount = redis.call("HINCRBY", key, userId, -1)
if tonumber(currentCount) <= 0 then
    redis.call("HDEL", key, userId)
end
return currentCount