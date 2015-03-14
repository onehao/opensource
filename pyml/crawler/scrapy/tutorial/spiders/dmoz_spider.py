# -*- coding: utf-8 -*-
'''
@author: Michael Wan
@since: 2015-01-13
@note: Spider是用户自己编写的类，用来从一个域（或域组）中抓取信息。

他们定义了用于下载的URL列表、跟踪链接的方案、解析网页内容的方式，以此来提取items。

要建立一个Spider，你必须用scrapy.spider.BaseSpider创建一个子类，并确定三个强制的属性：

name：爬虫的识别名称，必须是唯一的，在不同的爬虫中你必须定义不同的名字。
start_urls：爬取的URL列表。爬虫从这里开始抓取数据，所以，第一次下载的数据将会从这些urls开始。其他子URL将会从这些起始URL中继承性生成。
parse()：解析的方法，调用的时候传入从每一个URL传回的Response对象作为唯一参数，负责解析并匹配抓取的数据(解析为item)，跟踪更多的URL。

@see: http://doc.scrapy.org/en/latest/intro/tutorial.html

'''


from scrapy.selector.unified import Selector
from scrapy.spider import BaseSpider
from tutorial.items import Dmozitem


class DmozSpider(BaseSpider):
    name = "dmoz"
    allowed_domains = ["dmoz.org"]
    start_urls = [
        "http://www.dmoz.org/Computers/Programming/Languages/Python/Books/",
        "http://www.dmoz.org/Computers/Programming/Languages/Python/Resources/"
    ]

    def parse(self, response):
        sel = Selector(response)
        sites = sel.xpath('//ul[@class="directory-url"]/li')
        items = []
        for site in sites:
            item = Dmozitem()
            item['title'] = site.xpath('a/text()').extract()
            item['link'] = site.xpath('a/@href').extract()
            item['desc'] = site.xpath('text()').extract()
            items.append(item)
        return items
