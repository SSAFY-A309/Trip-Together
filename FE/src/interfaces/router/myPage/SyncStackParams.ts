import {ParamListBase} from '@react-navigation/native';
import {bankAccount} from '../../bankAccount';

interface SyncConfirmProps {
  selected: bankAccount;
}

interface SyncStackParams extends ParamListBase {
  SyncMain: undefined;
  SyncSelect: undefined;
  SyncConfirm: SyncConfirmProps;
  SyncComplete: undefined;
}

export type {SyncConfirmProps, SyncStackParams};
