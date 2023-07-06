<template>
  <t-card :bordered="false">
    <t-form
      ref="form"
      :data="formData"
      :rules="FORM_RULES"
      label-align="top"
      :label-width="100"
      @reset="onReset"
      @submit="onSubmit"
    >
      <div class="form-basic-container">
        <div class="form-basic-item">
          <div class="form-basic-container-title">数据库信息</div>
          <!-- 表单内容 -->

          <!-- 合同名称,合同类型 -->
          <t-row class="row-gap" :gutter="[16, 12]">
            <t-col :span="8">
              <t-form-item label="数据库类型" name="type">
                <t-select
                  v-model="formData.type"
                  :style="{ width: '322px' }"
                  placeholder="请选择类型"
                  class="demo-select-base"
                  clearable
                >
                  <t-option v-for="(item, index) in typeOptions" :key="index" :value="item.value" :label="item.label">
                    {{ item.label }}
                  </t-option>
                </t-select>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="服务器地址IP" name="type">
                <t-input placeholder="请输入服务器地址IP"/>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="端口号" name="type">
                <t-input :style="{ width: '120px' }" placeholder="请输入端口号"/>
              </t-form-item>
            </t-col>
            <!-- 合同收付类型 -->
            <t-col :span="8">
              <t-form-item label="用户名" name="payment">
                <t-input placeholder="请输入用户名"/>
              </t-form-item>
            </t-col>

            <t-col :span="8">
              <t-form-item label="密码" name="partyA">
                <t-input placeholder="请输入密码"/>
              </t-form-item>
            </t-col>
            <t-col :span="8">
              <t-form-item label="数据库" name="partyB">
                <t-input placeholder="请输入数据库名称"/>
              </t-form-item>
            </t-col>
            <t-col :span="8">
              <t-form-item label="Schema" name="signDate">
                <t-input placeholder="请输入Schema"/>
              </t-form-item>
            </t-col>
          </t-row>
          <t-form-item label="备注" name="comment">
            <t-textarea v-model="formData.comment" :height="124" placeholder="请输入备注"/>
          </t-form-item>
        </div>
      </div>
      <div class="form-submit-container">
        <div class="form-submit-sub">
          <div class="form-submit-left">
            <t-button theme="primary" class="form-submit-confirm" type="submit"> 提交</t-button>
            <t-button type="reset" class="form-submit-cancel" theme="default" variant="base"> 取消</t-button>
          </div>
        </div>
      </div>
    </t-form>
  </t-card>
</template>
<script>
import {prefix} from '@/config/global';

const INITIAL_DATA = {
  name: '',
  type: '',
  partyA: '',
  partyB: '',
  signDate: '',
  startDate: '',
  endDate: '',
  payment: '1',
  amount: 0,
  comment: '',
  files: [],
};
const FORM_RULES = {
  name: [{required: true, message: '请输入合同名称', type: 'error'}],
  type: [{required: true, message: '请选择合同类型', type: 'error'}],
  payment: [{required: true, message: '请选择合同收付类型', type: 'error'}],
  amount: [{required: true, message: '请输入合同金额', type: 'error'}],
  partyA: [{required: true, message: '请选择甲方', type: 'error'}],
  partyB: [{required: true, message: '请选择乙方', type: 'error'}],
  signDate: [{required: true, message: '请选择日期', type: 'error'}],
  startDate: [{required: true, message: '请选择日期', type: 'error'}],
  endDate: [{required: true, message: '请选择日期', type: 'error'}],
};

export default {
  name: 'FormBase',
  data() {
    return {
      prefix,
      stepSuccess: true,
      formData: {...INITIAL_DATA},
      FORM_RULES,
      typeOptions: [
        {label: 'MySql', value: '1'},
        {label: 'Oracle', value: '2'},
        {label: 'SqlServer', value: '3'},
      ],
      partyAOptions: [
        {label: '公司A', value: '1'},
        {label: '公司B', value: '2'},
        {label: '公司C', value: '3'},
      ],
      partyBOptions: [
        {label: '公司A', value: '1'},
        {label: '公司B', value: '2'},
        {label: '公司C', value: '3'},
      ],
      textareaValue: '',
      rules: {
        name: [{required: true, message: '请输入合同名称', type: 'error'}],
        type: [{required: true, message: '请选择合同类型', type: 'error'}],
        payment: [{required: true, message: '请选择合同收付类型', type: 'error'}],
        amount: [{required: true, message: '请输入合同金额', type: 'error'}],
        partyA: [{required: true, message: '请选择甲方', type: 'error'}],
        partyB: [{required: true, message: '请选择乙方', type: 'error'}],
        signDate: [{required: true, message: '请选择日期', type: 'error'}],
        startDate: [{required: true, message: '请选择日期', type: 'error'}],
        endDate: [{required: true, message: '请选择日期', type: 'error'}],
      },
    };
  },
  methods: {
    handleFail({file}) {
      this.$message.error(`文件 ${file.name} 上传失败`);
    },
    beforeUpload(file) {
      if (!/\.(pdf)$/.test(file.name)) {
        this.$message.warning('请上传pdf文件');
        return false;
      }
      return true;
    },
    // 用于格式化接口响应值，error 会被用于上传失败的提示文字；url 表示文件/图片地址
    formatResponse(res) {
      return {...res, error: '上传失败，请重试', url: res.url};
    },
    changeStatus() {
      this.stepSuccess = !this.stepSuccess;
    },
    onReset() {
      this.$message.warning('取消新建');
    },
    onSubmit({validateResult}) {
      if (validateResult === true) {
        this.$message.success('新建成功');
      }
    },
  },
};
</script>
<style lang="less" scoped>
@import './index';
</style>
