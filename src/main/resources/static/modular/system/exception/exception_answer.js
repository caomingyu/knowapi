layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax', 'util'], function () {
        var layer = layui.layer;
        var form = layui.form;
        var table = layui.table;
        var util = layui.util;

        /**
         * 系统管理--异常管理
         */
        var MgrException = {
            tableId: "answerTable"    //表格id
        };

        /**
         * 初始化表格的列
         */
        MgrException.initColumn = function () {
            return [[
                {type: 'checkbox'},
                {field: 'aid', hide: true, sort: true, title: '异常方案id'},
                {field: 'id', hide: true, sort: true, title: '异常方案信息id'},
                {field: 'exceptionCode', sort: true, title: '异常编码'},
                {field: 'flowType', sort: true, title: '环节编码'},
                {field: 'exceptionType', sort: true, title: '异常类型'},
                {field: 'tittle', sort: true, title: '异常方案标题'},
                {field: 'author', sort: true, title: '创建人'},
                {
                    field: 'createTime', sort: true, title: '创建时间', templet: function (d) {
                        return util.toDateString(d.createTime, "yyyy-MM-dd HH:mm:ss");
                    }
                },
                {
                    field: 'passTime', sort: true, title: '审核时间', templet: function (d) {
                        return util.toDateString(d.passTime, "yyyy-MM-dd HH:mm:ss");
                    }
                },
                {field: 'state', sort: true, templet: '#statusTpl', title: '审核状态'},
                {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 280}
            ]];
        };
        /**
         * 点击查询按钮
         */
        MgrException.search = function () {
            var queryData = {};
            queryData['param'] = $("#name").val();
            table.reload(MgrException.tableId, {
                where: queryData
            });
        };

        /**
         * 导出excel按钮
         */
        MgrException.exportExcel = function () {
            var checkRows = table.checkStatus(MgrException.tableId);
            if (checkRows.data.length === 0) {
                Feng.error("请选择要导出的数据");
            } else {
                table.exportFile(tableResult.config.id, checkRows.data, 'xls');
            }
        };

        /**
         * 点击删除异常按钮
         *
         * @param data 点击按钮时候的行数据
         */
        MgrException.onDeleteException = function (data) {
            layer.confirm("是否删除异常" + data.exceptionCode + "的解决方案?", {bet: ["确认", "取消"]},
                function () {
                    debugger;
                    $.ajax({
                        url: "/answer/del",
                        type: "post",
                        data: {"id": data.aid},
                        success: function (info) {
                            var result = JSON.parse(info);
                            if (result.data == "true") {
                                table.reload(MgrException.tableId);
                            }
                            layer.msg(result.msg, {
                                icon: 1, time: 2000, end: function () {
                                    var index = parent.layer.getFrameIndex(window.name); // 先得到当前 iframe 层的索引
                                    parent.layer.close(index); // 再执行关闭
                                }
                            });
                        },
                        error: function (info) {
                            console.log(info)
                        }
                    });
                    return false;
                });
        };

        /**
         * 查看详细信息按钮
         *
         * @param data 点击按钮时候的行数据
         */
        MgrException.showDetail = function (data) {
            layer.open({
                type: 2,
                title: '详细信息查看',
                area: ['300px', '400px'],
                content: '/answer/detail?infoId=' + data.id,
                // end: function () {
                //     table.reload(MgrException.tableId);
                // }
            });
        };

        /**
         * 修改审核状态
         *
         * @param userId 用户id
         * @param checked 是否选中（true,false），选中就是解锁用户，未选中就是锁定用户
         */
        MgrException.changeUserStatus = function (id, checked) {
            debugger;
            var data = {};
            data['id'] = id;
            if (checked) {
                data['state'] = 1;
            } else {
                data['state'] = 0;
            }
            $.ajax({
                url: "/answer/audite",
                type: "post",
                data: data,
                success: function (info) {
                    debugger;
                    var result = JSON.parse(info);
                    if (result.data == "true") {
                        Feng.success("审核状态切换成功!");
                    } else {
                        Feng.error("审核状态切换失败!" + info.result + "!");
                    }
                    table.reload(MgrException.tableId);
                }
            });
        };
        debugger;
        // 渲染表格
        var tableResult = table.render({
            elem: "#answerTable",
            url: '/answer/list',
            page: true,
            request: {
                pageName: 'pageNum'
            },
            height: "full-158",
            cellMinWidth: 100,
            cols: MgrException.initColumn()
        });

// 搜索按钮点击事件
        $('#btnSearch').click(function () {
            MgrException.search();
        });

// 导出excel
        $('#btnExp').click(function () {
            MgrException.exportExcel();
        });

// 工具条点击事件
        table.on('tool(' + MgrException.tableId + ')', function (obj) {
            console.log(obj.data);
            var data = obj.data;
            var layEvent = obj.event;
            if (layEvent === 'delete') {
                MgrException.onDeleteException(data);
            } else if (layEvent === 'showDetail') {
                MgrException.showDetail(data);
            }
        });

        // 修改审核状态
        form.on('switch(status)', function (obj) {

            var answerId = obj.elem.value;
            var checked = obj.elem.checked ? true : false;

            MgrException.changeUserStatus(answerId, checked);
        });

    }
)
;
