local key = KEYS[1]
local keyword = ARGV[1]
local currentCount = redis.call("HINCRBY", key, keyword, -1)
if tonumber(currentCount) <= 0 then
    redis.call("HDEL", key, keyword)
end
return currentCount