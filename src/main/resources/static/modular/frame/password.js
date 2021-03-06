layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;
    var admin = layui.admin;
    var $ax = layui.ax;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();

    // 监听提交
    form.on('submit(submit-psw)', function (data) {
        debugger;
        $.ajax({
            url: "mgr/psw_change",
            type: "post",
            data: data.field,
            success: function (info) {
                debugger;
                var result = JSON.parse(info);
                if (result.data == "true") {
                    layer.msg("修改成功,需要重新登录", {
                        icon: 1,
                        time: 2000,
                        end: function () {
                            location.href = '/logout';
                        }
                    });
                } else {
                    layer.msg(result.msg, {icon: 5, anim: 6});
                }
            }
        });
        // var ajax = new $ax("/psw_change", function (data) {
        //     Feng.success("修改成功!");
        //     admin.closeThisDialog();
        // }, function (data) {
        //     Feng.error("修改失败!" + data.responseJSON.message + "!");
        // });
        // ajax.setData(data.field);
        // ajax.start();

        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    // 添加表单验证方法
    form.verify({
        psw: [/^[\S]{6,12}$/, '密码必须6到12位，且不能出现空格'],
        repsw: function (t) {
            if (t !== $('#form-psw input[name=newPassword]').val()) {
                return '两次密码输入不一致';
            }
        }
    });
});