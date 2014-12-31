# coding=utf8

''' hackerrank'''
''' python version 2.7 '''
'''
@author: Michael Wan
@since: 2014-12-31
@requires: xhtml2pdf https://pypi.python.org/pypi/xhtml2pdf/, reportlab, html5lib, PyPDF(optional)
'''

from newspdf.xhtml2pdf import pisa


data = open('test.html').read()
result = file('test.pdf','wb')
pdf = pisa.CreatePDF(data, result)
result.close()
pisa.startViewer('test.pdf')