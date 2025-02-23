local key = KEYS[1]
local email = ARGV[1]
local currentCount = redis.call("HINCRBY", key, email, -1)
if tonumber(currentCount) <= 0 then
    redis.call("HDEL", key, email)
end
return currentCount