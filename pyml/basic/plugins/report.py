# -*- coding: utf-8 -*-
'''
Created on 2016年5月18日

@author: michael.wh
'''
import pytest


@pytest.mark.tryfirst
def pytest_runtest_makereport(item, call, __multicall__):
    # execute all other hooks to obtain the report object
    rep = __multicall__.execute()
    if rep.when == "call":
        extra = '{0}::{1} -- "{2}"'.format(item._location[0], item._location[2], '' if item._obj.__doc__ is None else item._obj.__doc__.strip())
        rep.nodeid =  extra
    return rep

def pytest_addoption(parser):
    parser.addoption("--ips", action="store", default="type1",
        help="my option: type1 or type2")
    
@pytest.fixture
def ips(request):
    return request.config.getoption("--ips")