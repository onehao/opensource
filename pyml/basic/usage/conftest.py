# -*- coding: utf-8 -*-
'''
Created on 2016年5月18日

@author: michael.wh
'''
import sys

import pytest


sys.path.append('../plugins')
pytest_plugins = "report"
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