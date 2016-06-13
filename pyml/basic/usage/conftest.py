# -*- coding: utf-8 -*-
'''
Created on 2016年5月18日

@author: michael.wh
'''
import sys

import pytest

sys.path.append('../plugins')
#pytest_plugins = "report"
# @pytest.mark.tryfirst
# def pytest_runtest_makereport(item, call, __multicall__):
#     # execute all other hooks to obtain the report object
#     rep = __multicall__.execute()
#     if rep.when == "call":
#         extra = '{0}::{1} -- "{2}"'.format(item._location[0], item._location[2], '' if item._obj.__doc__ is None else item._obj.__doc__.strip())
#         rep.nodeid = extra
#     if rep.when == "setup":
#         rep.nodeid = ""
#     return rep
# @pytest.hookimpl(tryfirst=True)
# def pytest_collection_modifyitems(session, config, items):
#     """ called after collection has been performed, may filter or re-order
#     the items in-place."""
#     print(str(items))
#     pass

@pytest.mark.tryfirst
def pytest_terminal_summary(terminalreporter):
    # execute all other hooks to obtain the report object
    print(str(terminalreporter))
    pass

def pytest_addoption(parser):
    parser.addoption("--ips", action="store", default="type1",
        help="my option: type1 or type2")
    
@pytest.fixture(scope = "module")
def ips(request):
    return request.config.getoption("--ips")

# @pytest.fixture(scope="session", autouse=True)
# def callattr_ahead_of_alltests(request):
#     print ("callattr_ahead_of_alltests called")
#     seen = set([None])
#     session = request.node
#     for item in session.items:
#         cls = item.getparent(pytest.Class)
#         cls._nodeid = item._obj.__doc__.strip()
#         if cls not in seen:
#             if hasattr(cls.obj, "callme"):
#                 cls.obj.callme()
#             seen.add(cls)