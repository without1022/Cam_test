### 注意事项
framework还有2处限制需要去除
1.高通添加的packagelist限制，只有指定应用可以读取三个以上的cam
  验证方案：`adb pull system/build.prop .` 添加应用包名（**包名之间以,隔开**）再重新push到系统里；`adb push build.prop system/`;
      ```
        #Expose aux camera for below packages
        vendor.camera.aux.packagelist=org.codeaurora.snapcam,com.example.myapplication
      ```
2.应用对build.prop文件的读取权限（selinux）
    验证方案：开机后暂时关闭selinux验证；
    解决方案：修改selinux权限;
