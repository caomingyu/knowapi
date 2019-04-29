layui.use(['form', 'upload', 'element', 'ax', 'laydate', 'util'], function () {
    var $ = layui.jquery;
    var form = layui.form;
    var upload = layui.upload;
    var element = layui.element;
    var $ax = layui.ax;
    var laydate = layui.laydate;
    var util = layui.util;

    //渲染时间选择框
    laydate.render({
        elem: '#birthday'
    });


    $.ajax({
        url: "/userDetail",
        type: "post",
        data: {},
        success: function (info) {
            debugger;
            var result = JSON.parse(info);
            console.log(result);
            result.birthDay = util.toDateString(result.birthDay, "yyyy-MM-dd");
            console.log(result);
            form.val('userInfoForm', result);
        }
    });
    //表单提交事件
    form.on('submit(userInfoSubmit)', function (data) {
        $.ajax({
            url: "/userInfoEdit",
            type: "post",
            data: data.field,
            success: function (info) {
                var result = JSON.parse(info);
                if (result.data == "true") {
                    layer.msg(result.msg);
                    location.reload();
                } else {
                    layer.msg(result.msg)
                }
            }
        });
        return false;
    });

    upload.render({
        elem: '#imgHead',
        url: '', // 上传接口
        done: function (res) {
            // 上传完毕回调
        },
        error: function () {
            // 请求异常回调
        }
    });
});