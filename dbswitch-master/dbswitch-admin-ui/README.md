# dbswitch-admin-ui

## 一、介绍

基于Vue.js 2.0编写的dbswitch操作管理web端。

## 二、环境

 **node** : >= v14.15.4
 
### 1、CentOS下安装Nodejs
 
```
# 下载nodejs
wget https://nodejs.org/dist/v14.15.4/node-v14.15.4-linux-x64.tar.xz
# 解压缩
tar -xvf node-v14.15.4-linux-x64.tar.xz && mkdir -p /usr/local/nodejs && mv node-v14.15.4-linux-x64/* /usr/local/nodejs/
# 建立node软链接
ln -s /usr/local/nodejs/bin/node /usr/local/bin
# 建立npm 软链接
ln -s /usr/local/nodejs/bin/npm /usr/local/bin
# 设置国内淘宝镜像源
npm config set registry https://registry.npm.taobao.org
# 查看设置信息
npm config list
# 验证是否安装成功
node -v
npm -v
```

### 2. Windows下安装Nodejs

可参考[博文教程](https://zhuanlan.zhihu.com/p/572795586)

## 二、构建

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report
```

## 三、部署

执行`npm run build`命令后，将dbswitch-admin-ui\dist目录生成的所有文件拷贝（或替换）到dbswitch-admin\src\main\resources目录下。然后直接使用mvn对整个dbswitch项目打包即可。