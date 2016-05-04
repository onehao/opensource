# -*- coding:utf-8 -*-
'''
Created on 2015年3月12日

@author: wanhao01
'''

import os


class SpiderFileUtils(object):
    '''
    deal with file related operations.
    '''
    
    def __save_page(self, data, url, outputdir):
        '''
        save the page content with the specific url to the local path.
        '''
        if(not os.path.exists(outputdir)):
            os.makedirs(outputdir)
        filename = self.__validate_name(url)
        f = open(outputdir + os.sep + filename, 'w')
        f.writelines(data)
        f.close()

if __name__ == '__main__':
    pass