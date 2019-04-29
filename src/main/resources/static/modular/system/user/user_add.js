/**
 * 用户详情对话框
 */
var UserInfoDlg = {
    data: {
        deptId: "",
        deptName: ""
    }
};

layui.use(['layer', 'form', 'admin', 'laydate', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();

    // 添加表单验证方法
    form.verify({
        psw: [/^[\S]{6,12}$/, '密码必须6到12位，且不能出现空格'],
        repsw: function (value) {
            if (value !== $('#userForm input[name=password]').val()) {
                return '两次密码输入不一致';
            }
        }
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        $.ajax({
            url: "/doRegist",
            type: "post",
            data: data.field,
            success: function (info) {
                debugger;
                var result = JSON.parse(info);
                if (result.data == "true") {
                    layer.msg(result.msg, {
                        icon: 1, time: 2000, end: function () {
                            var index = parent.layer.getFrameIndex(window.name); // 先得到当前 iframe 层的索引
                            parent.layer.close(index); // 再执行关闭
                        }
                    });
                } else {
                    layer.msg(result.msg, {icon: 5, anim: 6});
                }
            }
        });
        return false;
    });
});