<template>
  <t-card :bordered="false">
    <t-dialog width="50%" maxHeight="100px" :visible.sync="visible">
      <t-table
        rowKey="index"
        :data="data"
        :footData="[{}]"
        :columns="columns"
        :table-layout="tableLayout"
        :table-content-width="tableLayout === 'fixed' ? undefined : '1600px'"
        :max-height="fixedTopAndBottomRows ? 500 : 300"
        :fixedRows="fixedTopAndBottomRows ? [2, 2] : undefined"
        :scroll="virtualScroll ? { type: 'virtual' } : undefined"
        :stripe="stripe"
        :selected-row-keys="selectedRowKeys"
        @select-change="rehandleSelectChange"
        bordered
        size="small"
      >
      </t-table>
    </t-dialog>
    <div class="form-step-container">
      <!-- 简单步骤条 -->
      <t-card :bordered="false">
        <t-steps :defaultCurrent="1" :current="activeForm" status="process">
          <t-step-item title="任务摘要" content="基本信息"></t-step-item>
          <t-step-item title="电子发票" content="预计1～3个工作日"></t-step-item>
          <t-step-item title="发票已邮寄" content="电子发票开出后7个工作日内联系"></t-step-item>
          <t-step-item title="完成" content=""></t-step-item>
        </t-steps>
      </t-card>

      <!-- 分步表单1 :rules="rules" -->
      <t-form
        v-show="activeForm === 0"
        class="step-form"
        :data="formData2"
        labelAlign="right"
        @submit="onSubmit1"
        colon
      >
        <t-form-item label="任务名称" name="title">
          <t-input :style="{ width: '480px' }" v-model="formData2.title" placeholder="请输入任务名称"></t-input>
        </t-form-item>
        <t-form-item label="任务描述" name="description">
          <t-textarea :style="{ width: '480px' }" v-model="formData2.description"
                      :maxcharacter="200" placeholder="请输入任务描述"></t-textarea>
        </t-form-item>
        <t-form-item label="源数" name="source">
          <t-select :style="{ width: '390px' }" v-model="formData2.source" class="demo-select-base" clearable>
            <t-option v-for="(item, index) in sourceOptions" :value="item.value" :label="item.name" :key="index">
              {{ item.name }}
            </t-option>
          </t-select>
        </t-form-item>
        <t-form-item label="目标库" name="target">
          <t-select :style="{ width: '390px' }" v-model="formData2.target" class="demo-select-base" clearable>
            <t-option v-for="(item, index) in targetOptions" :value="item.value" :label="item.name" :key="index">
              {{ item.name }}
            </t-option>
          </t-select>
        </t-form-item>
        <t-form-item>
          <t-button theme="primary" type="submit">下一步</t-button>
        </t-form-item>
      </t-form>

      <!-- 分步表单2 -->
      <t-form
        v-show="activeForm === 1"
        class="step-form"
        :data="formData1"
        :rules="rules"
        labelAlign="right"
        @reset="onReset2"
        @submit="onSubmit2"
        colon
      >
        <t-form-item class="amount-label" label="迁移表">

            <t-tag theme="primary" variant="light">21 / 50</t-tag>
            <t-button @click="visible = true" style="margin-left: 50px">配置迁移表</t-button>

        </t-form-item>

        <t-form-item label="迁移表设置" name="type">
          <t-checkbox-group>
            <t-checkbox value="1">迁移表结构</t-checkbox>
            <t-checkbox value="2">迁移数据</t-checkbox>
            <t-checkbox value="3">重建表</t-checkbox>
          </t-checkbox-group>
        </t-form-item>
        <t-form-item label="迁移设置" name="name">
          <t-checkbox-group v-model="value2">
            <t-checkbox :checkAll="true" label="全选"/>
            <t-checkbox label="选项一" value="选项一"/>
            <t-checkbox label="选项二" value="选项二"/>
            <t-checkbox label="选项三" value="选项三"/>
          </t-checkbox-group>
        </t-form-item>
        <t-form-item label="发票类型" name="type">
          <t-checkbox-group>
            <t-checkbox value="1">语文</t-checkbox>
            <t-checkbox value="2">数学</t-checkbox>
            <t-checkbox value="3">英语</t-checkbox>
            <t-checkbox value="4">体育</t-checkbox>
          </t-checkbox-group>
        </t-form-item>

        <t-form-item>
          <t-button type="reset" theme="default" variant="base">上一步</t-button>
          <t-button theme="primary" type="submit">下一步</t-button>
        </t-form-item>
      </t-form>


      <!-- 分步表单3 -->
      <t-form
        v-show="activeForm === 2"
        class="step-form"
        :data="formData3"
        labelAlign="left"
        @reset="onReset3"
        @submit="onSubmit3"
      >
        <t-form-item label="收货人" name="consignee">
          <t-input :style="{ width: '480px' }" v-model="formData3.consignee" placeholder="请输入收货人"></t-input>
        </t-form-item>
        <t-form-item label="手机号码" name="mobileNum">
          <t-input :style="{ width: '480px' }" v-model="formData3.mobileNum" placeholder="请输入手机号码"></t-input>
        </t-form-item>
        <t-form-item label="收货地址" name="deliveryAddress">
          <t-select
            :style="{ width: '480px' }"
            placeholder="请选择收货地址"
            v-model="formData3.deliveryAddress"
            class="demo-select-base"
            clearable
          >
            <t-option v-for="(item, index) in addressOptions" :value="item.value" :label="item.label" :key="index">
              {{ item.label }}
            </t-option>
          </t-select>
        </t-form-item>
        <t-form-item label="详细地址" name="fullAddress">
          <t-textarea
            :style="{ width: '480px' }"
            v-model="formData3.fullAddress"
            placeholder="请输入详细地址"
          ></t-textarea>
        </t-form-item>
        <t-form-item>
          <t-button type="reset" theme="default" variant="base">上一步</t-button>
          <t-button theme="primary" type="submit">下一步</t-button>
        </t-form-item>
      </t-form>

      <!-- 分步表单4 -->
      <div class="step-form-4" v-show="activeForm === 6">
        <check-circle-filled-icon style="color: green" size="52px"/>
        <p class="text">完成开票申请</p>
        <p class="tips">预计1～3个工作日会将电子发票发至邮箱，发票邮寄请耐心等待</p>
        <div class="button-group">
          <t-button @click="onReset4" theme="primary">再次申请</t-button>
          <t-button @click="onSubmit4" variant="base" theme="default">查看进度</t-button>
        </div>
      </div>
    </div>
  </t-card>
</template>
<script lang="jsx">
import {CheckCircleFilledIcon} from 'tdesign-icons-vue';
import {prefix} from '@/config/global';
import { ErrorCircleFilledIcon, CloseCircleFilledIcon } from 'tdesign-icons-vue';
const INITIAL_DATA1 = {
  name: '',
  type: '',
};
const INITIAL_DATA2 = {
  title: '',
  description: '',
  source: '',
  target: ''
};
const INITIAL_DATA3 = {
  consignee: '',
  mobileNum: '',
  deliveryAddress: '',
  fullAddress: '',
};
const data = new Array(10).fill(null).map((_, i) => ({
  index: i + 1,
  sourceTableName: ['AA', 'BB', 'CC'][i % 3],
  targetTableName: ['AA', 'BB', 'CC'][i % 3],
  tableType: '普通表',
  dataCount: '10000',
}));
export default {
  name: 'FormStep',
  components: {
    CheckCircleFilledIcon,
    ErrorCircleFilledIcon,
    CloseCircleFilledIcon
  },
  data() {
    return {
      prefix,
      formData1: {...INITIAL_DATA1},
      formData2: {...INITIAL_DATA2},
      formData3: {...INITIAL_DATA3},
      sourceOptions: [
        {name: 'MySql', value: '1'},
        {name: 'Oracle', value: '2'},
        {name: 'SqlServer', value: '3'},
      ],
      targetOptions: [
        {name: 'MySql', value: '1'},
        {name: 'Oracle', value: '2'},
        {name: 'SqlServer', value: '3'},
      ],
      value2: ['选项一'],
      addressOptions: [
        {label: '广东省深圳市南山区', value: '1'},
        {label: '北京市海淀区', value: '2'},
        {label: '上海市徐汇区', value: '3'},
        {label: '四川省成都市高新区', value: '4'},
        {label: '广东省广州市天河区', value: '5'},
        {label: '陕西省西安市高新区', value: '6'},
      ],
      rules: {
        source: [{required: true, message: '请选择源数据库类型', type: 'error'}],
        target: [{required: true, message: '请选择目标数据库类型', type: 'error'}],
        title: [{required: true, message: '请输入任务名称', type: 'error'}],
        taxNum: [{required: true, message: '请输入纳税人识别号', type: 'error'}],
        consignee: [{required: true, message: '请输入收货人', type: 'error'}],
        mobileNum: [{required: true, message: '请输入手机号码', type: 'error'}],
        deliveryAddress: [{required: true, message: '请选择收货地址', type: 'error'}],
        fullAddress: [{required: true, message: '请输入详细地址', type: 'error'}],
      },
      activeForm: 0,
      visible:false,
      virtualScroll: false,
      fixedTopAndBottomRows: false,
      stripe: false,
      tableLayout: 'fixed',
      data,
      columns: [
        {
          colKey: 'row-select',
          type: 'multiple',
          width: 50,
        },
        {
          colKey: 'sourceTableName',
          title: '源表名',
          width: '100',
          foot: '共20条',
        },
        {
          colKey: 'targetTableName',
          title: '目标表名',
          width: '100',
          fixed: 'left',
        },
        {
          colKey: 'tableType',
          title: '类型',
          width: '120',
          foot: '-',
        },
        {
          colKey: 'dataCount',
          title: '表记录数',
          width: '150',
          foot: '-',
        }
      ],
      selectedRowKeys:[]
    };
  },
  computed: {

  },
  methods: {
    rehandleClickOp(context) {
      console.log(context);
    },
    rehandleSelectChange(value, { selectedRowData }) {
      this.selectedRowKeys = value;
      console.log(value, selectedRowData);
    },
    onSubmit1({validateResult}) {
      if (validateResult === true) {
        this.activeForm = 1;
      }
    },
    onSubmit2({validateResult}) {
      if (validateResult === true) {
        this.activeForm = 2;
      }
    },
    onReset2() {
      this.activeForm = 0;
    },
    onSubmit3({validateResult}) {
      if (validateResult === true) {
        this.activeForm = 6;
      }
    },
    onReset3() {
      this.activeForm = 1;
    },
    onSubmit4() {
      this.$router.replace({path: '/detail/advanced'});
    },
    onReset4() {
      this.activeForm = 0;
    },
  },
};
</script>
<style lang="less" scoped>
@import './index';
.link {
  cursor: pointer;
}
</style>
