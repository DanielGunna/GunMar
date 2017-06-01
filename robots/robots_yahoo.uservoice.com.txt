# See http://www.robotstxt.org/wc/norobots.html for documentation on how to use the robots.txt file

# MSN bot is prone to over-crawling
User-agent: msnbot
Crawl-delay: 120

# YandexBot bot is prone to over-crawling
User-agent: YandexBot
Crawl-delay: 120

# 80legs is often abused
User-agent: 008
Disallow: /

# Disabled due to abuse
User-agent: TurnitinBot
Disallow: /

# Disabled due to abuse
User-agent: WBSearchBot
Disallow: /

# Disabled due to abuse
User-agent: SearchmetricsBot
Disallow: /

# Please don't crawl the API
User-agent: *
Disallow: /api/
Disallow: /admin/
