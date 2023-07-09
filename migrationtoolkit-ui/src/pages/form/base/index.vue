<template>
  <t-card :bordered="false">
    <t-form ref="form" :data="formData" :rules="FORM_RULES" label-align="top" :label-width="100" @reset="onReset"
      @submit="onSubmit">
      <div class="form-basic-container">
        <div class="form-basic-item">
          <div class="form-basic-container-title">数据库信息</div>
          <!-- 表单内容 -->
          <t-row class="row-gap" :gutter="[16, 12]">

            <t-col :span="8">
              <t-form-item label="数据库类型" name="dbtype">
                <t-select v-model="formData.dbtype" :style="{ width: '322px' }" placeholder="请选择类型"
                  class="demo-select-base" clearable>
                  <t-option v-for="(item, index) in dbTypeOptions" :key="index" :value="item.value" :label="item.label">
                    {{ item.label }}
                  </t-option>
                </t-select>
              </t-form-item>
            </t-col>
            <t-col :span="8">
              <t-form-item label="连接名称" name="connname">
                <t-input v-model="formData.connname" placeholder="请输入连接名称" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="服务器地址IP" name="host">
                <t-input v-model="formData.host" placeholder="请输入服务器地址IP" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="端口号" name="port">
                <t-input v-model="formData.port" :style="{ width: '120px' }" placeholder="请输入端口号" />
              </t-form-item>
            </t-col>
            <t-col :span="8">
              <t-form-item label="用户名" name="username">
                <t-input v-model="formData.username" placeholder="请输入用户名" />
              </t-form-item>
            </t-col>

            <t-col :span="8">
              <t-form-item label="密码" name="password">
                <t-input v-model="formData.password" placeholder="请输入密码" />
              </t-form-item>
            </t-col>
            <t-col :span="8">
              <t-form-item label="数据库(实例名/服务名)" name="dbname">
                <t-input v-model="formData.dbname" placeholder="请输入数据库名称" />
              </t-form-item>
            </t-col>
            <t-col :span="8">
              <t-form-item label="Schema(架构)" name="schema">
                <t-input v-model="formData.schema" placeholder="请输入Schema" />
              </t-form-item>
            </t-col>
          </t-row>
          <t-col :span="8">
            <t-form-item label="连接参数" name="connParam">
              <t-textarea v-model="formData.connParam" placeholder="请输入备注" />
            </t-form-item>
          </t-col>
          <t-form-item label="备注" name="comment">
            <t-textarea v-model="formData.comment" :height="124" placeholder="请输入备注" />
          </t-form-item>
        </div>
      </div>
      <div class="form-submit-container">
        <div class="form-submit-sub">
          <div class="form-submit-left">
            <t-button theme="primary" class="form-submit-confirm" type="submit"> 提交</t-button>
            <t-button type="reset" style="margin-left: 20px" class="form-submit-cancel" theme="default" variant="base">
              取消</t-button>
            <t-button theme="primary" style="margin-left: 20px" class="form-submit-confirm" @click="testConn()">
              测试链接</t-button>
          </div>
        </div>
      </div>
    </t-form>
  </t-card>
</template>
<script>
import { prefix } from '@/config/global';

const FORM_RULES = {
  connname: [{ required: true, message: '请输入连接名称', type: 'error' }],
  dbtype: [{ required: true, message: '请选择数据库类型', type: 'error' }],
  host: [{ required: true, message: '请输入服务器地址', type: 'error' }],
  port: [{ required: true, message: '请输入端口号', type: 'error' }],
  username: [{ required: true, message: '请输入用户名', type: 'error' }],
  password: [{ required: true, message: '请输入密码', type: 'error' }],
  dbname: [{ required: true, message: '请选择数据库', type: 'error' }],
};
export default {
  name: 'FormBase',
  data() {
    return {
      prefix,
      stepSuccess: true,
      formData: {
        connname: '',
        dbtype: 'Oracle',
        host: 'localhost',
        port: '3306',
        username: 'root',
        password: '0507',
        dbname: 'myblog',
        schema: '',
        comment: '',
        connParam: '',
      },
      FORM_RULES,
      dbTypeOptions: [],
      textareaValue: '',
    };
  },
  created() {
    this.getDbTypeOptions();
  },
  methods: {
    changeStatus() {
      this.stepSuccess = !this.stepSuccess;
    },
    onReset() {
      this.$message.warning('取消新建');
    },
    onSubmit({ validateResult }) {
      if (validateResult === true) {
        this.$request
          .post("/data-base/saveConnection", this.formData)
          .then((res) => {
            if (res.data.code === 200) {
              this.$message.success('保存成功！');
            }
            if (res.data.code === 500) {
              this.$message.error("保存失败！" + res.data.result);
            }
          })
          .catch((e) => {
            console.log(e);
          })
          .finally(() => { });
      }
    },
    testConn() {
      if (this.formData.dbtype != '' && this.formData.host != '' && this.formData.port != ''
        && this.formData.username != '' && this.formData.password != '' && this.formData.dbname != '') {
        this.$request
          .post("/data-base/testConnection", this.formData)
          .then((res) => {
            if (res.data.code === 200) {
              this.$message.success('连接成功！');
            }
            if (res.data.code === 500) {
              this.$message.error(res.data.result);
            }
          })
          .catch((e) => {
            console.log(e);
          })
          .finally(() => { });
      } else {
        this.$message.error('请填写必填项');
      }


    },
    getDbTypeOptions() {
      this.$request
        .get("/data-base/dataBaseType")
        .then((res) => {
          if (res.data.code === 200) {
            this.dbTypeOptions = res.data.result.map(option => ({
              label: option,
              value: option,
            }))
          }
        })
        .catch((e) => {
          console.log(e);
        })
        .finally(() => { });
    }
  },
};
</script>
<style lang="less" scoped>
@import './index';
</style>
