# ChinesePCCTVLocationExtraction

***
###This's a cool try, budy.

***
####Description
>Complete Chinese location infos in the format of province_city_county_town_village.

##Cautions of the methods calling order:
>###For the first use of this program, you have to run "readAllProv();" method first under the annotated method "getAllMaps();" in order to get all the property files.Then, annotate this method and call method "getAllMaps();".

>just like this:

![method calling order](https://github.com/scofield7419/ChinesePCCTVLocationExtraction/blob/master/screenshot/11.png)

####The roadmap is constructed in accordance with the following four guidelines:

- the datas was crawed from ["中华人民共和国国家统计局2015数据"](http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2015/index.html).
- the 3rd-party lib was [jsoup](http://www.open-open.com/jsoup/).
- Because of the data trafic constraint of the target server ["中华人民共和国国家统计局2015数据"](),I couldn't get all the datas at once program running.So I just design a approach by utilizing the property files and sovled the problem.

####here is the properties folder:
>properties/北京市.properties

####here is the outputs folder:
>outputs/province_city_county_town_village.txt

>and the output file was writed like this:

![output file](https://github.com/scofield7419/ChinesePCCTVLocationExtraction/blob/master/screenshot/12.png)

***
####other file in assets:
>assets/2015年全国城市省市县区行政级别对照表.xls

>assets/province_city_county.txt

>ps:formats in PCC.

>and it look like this:

![other file](https://github.com/scofield7419/ChinesePCCTVLocationExtraction/blob/master/screenshot/22.png)

***
```
Scofield.Phil

Email: feish7419@163.com

move fast, break things.
```

***











