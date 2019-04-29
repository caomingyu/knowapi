layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax'], function () {
        var layer = layui.layer;
        var form = layui.form;
        var table = layui.table;

        /**
         * 系统管理--用户管理
         */
        var MgrUser = {
            tableId: "userTable",    //表格id
            condition: {
                name: "",
                deptId: "",
                timeLimit: ""
            }
        };

        /**
         * 初始化表格的列
         */
        MgrUser.initColumn = function () {
            return [[
                {type: 'checkbox'},
                {field: 'uid', hide: true, sort: true, title: '用户id'},
                {field: 'id', hide: true, sort: true, title: '用户详细信息id'},
                {field: 'account', sort: true, title: '账号'},
                {field: 'name', sort: true, title: '姓名'},
                {field: 'sex', sort: true, title: '性别'},
                {field: 'email', sort: true, title: '邮箱'},
                {field: 'phone', sort: true, title: '电话'},
                {field: 'state', sort: true, templet: '#statusTpl', title: '状态'},
                {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 280}
            ]];
        };
        /**
         * 点击查询按钮
         */
        MgrUser.search = function () {
            var queryData = {};
            queryData['param'] = $("#name").val();
            table.reload(MgrUser.tableId, {
                where: queryData
            });
        };

        /**
         * 导出excel按钮
         */
        MgrUser.exportExcel = function () {
            var checkRows = table.checkStatus(MgrUser.tableId);
            if (checkRows.data.length === 0) {
                Feng.error("请选择要导出的数据");
            } else {
                table.exportFile(tableResult.config.id, checkRows.data, 'xls');
            }
        };

        // /**
        //  * 点击编辑用户按钮时
        //  *
        //  * @param data 点击按钮时候的行数据
        //  */
        // MgrUser.onEditUser = function (data) {
        //     admin.putTempData('formOk', false);
        //     top.layui.admin.open({
        //         type: 2,
        //         title: '编辑用户',
        //         content: Feng.ctxPath + '/mgr/user_edit?userId=' + data.userId,
        //         end: function () {
        //             admin.getTempData('formOk') && table.reload(MgrUser.tableId);
        //         }
        //     });
        // };

        /**
         * 点击删除用户按钮
         *
         * @param data 点击按钮时候的行数据
         */
        MgrUser.onDeleteUser = function (data) {
            layer.confirm("是否删除用户" + data.account + "?", {bet: ["确认", "取消"]},
                function (index) {
                    debugger;
                    $.ajax({
                        url: "/user/del",
                        type: "post",
                        data: {"id": data.id},
                        success: function (info) {
                            var result = JSON.parse(info);
                            if (result.data == "true") {
                                table.reload(MgrUser.tableId);
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
         * 分配角色
         *
         * @param data 点击按钮时候的行数据
         */
        MgrUser.roleAssign = function (data) {
            layer.open({
                type: 2,
                title: '角色分配',
                area: ['300px', '400px'],
                content: '/user/roleDispatch?userId=' + data.uid,
                end: function () {
                    table.reload(MgrUser.tableId);
                }
            });
        };

        /**
         * 重置密码
         *
         * @param data 点击按钮时候的行数据
         */
        MgrUser.resetPassword = function (id) {
            console.log(id);
            layer.confirm("是否重置密码为111111 ?", {btn: ['确认', '取消'], shade: false},
                function (index) {
                    debugger;
                    $.ajax({
                        url: "/user/pswReset",
                        type: "post",
                        data: {"id": id},
                        success: function (info) {
                            var result = JSON.parse(info);
                            layer.msg(result.msg, {
                                icon: 1, time: 2000, end: function () {
                                    var index = parent.layer.getFrameIndex(window.name); // 先得到当前 iframe 层的索引
                                    parent.layer.close(index); // 再执行关闭
                                }
                            });
                        },
                        error: function (info) {
                            console.log(info);
                        }
                    });
                    return false;
                });
        };

        /**
         * 修改用户状态
         *
         * @param userId 用户id
         * @param checked 是否选中（true,false），选中就是解锁用户，未选中就是锁定用户
         */
        MgrUser.changeUserStatus = function (userId, checked) {
            debugger;
            var data = {};
            data['id'] = userId;
            if (checked) {
                data['state'] = 1;
            } else {
                data['state'] = 0;
            }
            $.ajax({
                url: "/user/tofreeze",
                type: "post",
                data: data,
                success: function (info) {
                    debugger;
                    var result = JSON.parse(info);
                    if (result.data == "true") {
                        Feng.success("冻结状态切换成功!");
                    } else {
                        Feng.error("冻结状态切换失败!" + info.result + "!");
                    }
                    table.reload(MgrUser.tableId);
                }
            });
        };
        debugger;
        // 渲染表格
        var tableResult = table.render({
            elem: '#userTable',
            url: '/user/list',
            page: true,
            request: {
                pageName: 'pageNum'
            },
            height: "full-158",
            cellMinWidth: 100,
            cols: MgrUser.initColumn()
        });
// //初始化左侧部门树
//     var ztree = new $ZTree("deptTree", "/dept/tree");
//     ztree.bindOnClick(MgrUser.onClickDept);
//     ztree.init();

// 搜索按钮点击事件
        $('#btnSearch').click(function () {
            MgrUser.search();
        });

// 添加按钮点击事件
        $('#btnAdd').click(function () {
            layer.open({
                type: 2,
                area: ['700px', '450px'],
                fixed: false, //不固定
                maxmin: true,
                content: '/user_add',
                end: function () {
                    table.reload(MgrUser.tableId);
                }
            });
        });

// 导出excel
        $('#btnExp').click(function () {
            MgrUser.exportExcel();
        });

// 工具条点击事件
        table.on('tool(' + MgrUser.tableId + ')', function (obj) {
            debugger;
            console.log(obj.data);
            var data = obj.data;
            var layEvent = obj.event;
            if (layEvent === 'delete') {
                MgrUser.onDeleteUser(data);
            } else if (layEvent === 'roleAssign') {
                MgrUser.roleAssign(data);
            } else if (layEvent === 'reset') {
                MgrUser.resetPassword(data.uid);
            }
        });

// 修改user状态
        form.on('switch(status)', function (obj) {

            var userId = obj.elem.value;
            var checked = obj.elem.checked ? true : false;

            MgrUser.changeUserStatus(userId, checked);
        });

    }
)
;
