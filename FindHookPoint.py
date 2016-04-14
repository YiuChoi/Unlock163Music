import os
def findHook(path):
    for i in os.walk(path+os.sep+"smali"+os.sep+"com"+os.sep+"netease"+os.sep+"cloudmusic"):
        if "utils" not in i[0]:
            for j in i[2]:
                def openread(path,name):
                    fi=open(path+os.sep+name)
                    lines=fi.readlines()
                    if "utils" in lines[1]:
                        nam=name[0:name.find(".")]
                        CONS=path.split(os.sep)
                        length=len(lines[1])
                        HOOK=lines[1][lines[1].find("utils/")+6:length]
                        print "HOOK_UTILS = ","com.netease.cloudmusic.utils."+HOOK+"\r"
                        print "HOOK_CONSTRUCTOR = ","com.netease.cloudmusic."+CONS[-2]+"."+CONS[-1]+"."+nam+"\n\n"
                    fi.close()
                if j.find(".")<=2:
                    openread(i[0],j)
#print "version 3.3.1\r"
#findHook(r"D:\Android\output\c331")
#print "version 3.2.1\r"
#findHook(r"D:\Android\output\c321")
#print "version 3.1.4\r"
#findHook(r"D:\Android\output\c314")

