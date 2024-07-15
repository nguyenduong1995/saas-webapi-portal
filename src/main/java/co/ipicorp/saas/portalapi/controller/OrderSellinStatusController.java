/**
 * OrderSelloutController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import co.ipicorp.saas.core.model.Customer;
import co.ipicorp.saas.core.web.util.CommonUtils;
import co.ipicorp.saas.nrms.model.ImportType;
import co.ipicorp.saas.nrms.model.OrderSellin;
import co.ipicorp.saas.nrms.model.OrderSellinItem;
import co.ipicorp.saas.nrms.model.OrderSellinPromotionLimitationDetailReward;
import co.ipicorp.saas.nrms.model.OrderSellinStateChangeHistory;
import co.ipicorp.saas.nrms.model.OrderSellinStatus;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.RetailerWarehouseItemHistory;
import co.ipicorp.saas.nrms.model.Warehouse;
import co.ipicorp.saas.nrms.model.WarehouseDailyRemain;
import co.ipicorp.saas.nrms.model.WarehouseExportTicket;
import co.ipicorp.saas.nrms.model.WarehouseExportTicketItem;
import co.ipicorp.saas.nrms.model.WarehouseImportTicket;
import co.ipicorp.saas.nrms.model.WarehouseImportTicketItem;
import co.ipicorp.saas.nrms.model.WarehouseItem;
import co.ipicorp.saas.nrms.model.WarehouseItemHistory;
import co.ipicorp.saas.nrms.model.WarehouseTotalItem;
import co.ipicorp.saas.nrms.model.dto.OrderSellinCancelType;
import co.ipicorp.saas.nrms.service.OrderSellinItemService;
import co.ipicorp.saas.nrms.service.OrderSellinPromotionLimitationDetailRewardService;
import co.ipicorp.saas.nrms.service.OrderSellinService;
import co.ipicorp.saas.nrms.service.OrderSellinStateChangeHistoryService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseImportTicketService;
import co.ipicorp.saas.nrms.service.WarehouseDailyRemainService;
import co.ipicorp.saas.nrms.service.WarehouseExportTicketItemService;
import co.ipicorp.saas.nrms.service.WarehouseExportTicketService;
import co.ipicorp.saas.nrms.service.WarehouseImportTicketItemService;
import co.ipicorp.saas.nrms.service.WarehouseImportTicketService;
import co.ipicorp.saas.nrms.service.WarehouseItemHistoryService;
import co.ipicorp.saas.nrms.service.WarehouseItemService;
import co.ipicorp.saas.nrms.service.WarehouseService;
import co.ipicorp.saas.nrms.service.WarehouseTotalItemService;
import co.ipicorp.saas.nrms.service.util.ServiceUtils;
import co.ipicorp.saas.portalapi.form.OrderSellinForm;
import co.ipicorp.saas.portalapi.form.validator.AmountAvailableInWarehouseValidator;
import co.ipicorp.saas.portalapi.form.validator.AmountInWarehouseValidator;
import co.ipicorp.saas.portalapi.form.validator.OrderSellinExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.OrderSellinStateApprovedExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.OrderSellinStateDeliveredExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.OrderSellinStateNewExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.OrderSellinWarehouseExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.WarehouseTotalItemExistedValidator;
import co.ipicorp.saas.portalapi.security.CustomerSessionInfo;
import co.ipicorp.saas.portalapi.util.Constants;
import co.ipicorp.saas.portalapi.util.ControllerAction;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.util.RequestUtils;

/**
 * OrderSelloutController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
public class OrderSellinStatusController {

    private static final String EXPORT_TICKET_CODE_PREFIX = "WH";
    private static final String EXPORT_TICKET_CODE_MIDDLE = "EXP";
    
    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private OrderSellinService orderSellinService;

    @Autowired
    private OrderSellinItemService orderSellinItemService;

    @Autowired
    private WarehouseService warehouseService;
    
    @Autowired
    private WarehouseTotalItemService warehouseTotalItemService;

    @Autowired
    private OrderSellinStateChangeHistoryService osiStateChangeHistoryService;

    @Autowired
    private WarehouseItemService warehouseItemService;

    @Autowired
    private WarehouseItemHistoryService warehouseItemHistoryService;

    @Autowired
    private WarehouseExportTicketService warehouseExportTicketService;

    @Autowired
    private WarehouseExportTicketItemService warehouseExportTicketItemService;
    
    @Autowired
    private WarehouseImportTicketService warehouseImportTicketService;
    
    @Autowired
    private WarehouseImportTicketItemService warehouseImportTicketItemService;

    @Autowired
    private ProductVariationService productVariationService;
    
    @Autowired
    private RetailerWarehouseImportTicketService rwhImportTicketService;
    
    @Autowired
    private WarehouseDailyRemainService warehouseDailyRemainService;
    
    @Autowired
    private OrderSellinPromotionLimitationDetailRewardService rewardService;
    
    @PostMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLIN_STATE_ACTION + "/approved")
    @Validation(validators = { OrderSellinExistedValidator.class, AmountAvailableInWarehouseValidator.class, OrderSellinStateNewExistedValidator.class })
    @Logged
    public ResponseEntity<?> stateAprroved(HttpServletRequest request, HttpServletResponse response, @RequestBody OrderSellinForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                Customer customer = sessionInfo.getCustomer();
                OrderSellinStatusController.this.doChangeStatus(form, OrderSellinStatus.APPROVED, getRpcResponse(), (BindingResult) errors, customer);
                getRpcResponse().addAttribute("message", "successful");

            }
        };

        return support.doSupport(request, null, errors, errorsProcessor);
    }

    @PutMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLIN_STATE_ACTION + "/delivered")
    @Validation(validators = { OrderSellinWarehouseExistedValidator.class, OrderSellinStateApprovedExistedValidator.class, AmountInWarehouseValidator.class })
    @Logged
    public ResponseEntity<?> stateDelivered(HttpServletRequest request, HttpServletResponse response, @RequestBody OrderSellinForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                Customer customer = sessionInfo.getCustomer();
                OrderSellinStatusController.this.doChangeStatus(form, OrderSellinStatus.DELIVERED, getRpcResponse(), (BindingResult) errors, customer);
                getRpcResponse().addAttribute("message", "successful");
            }
        };

        return support.doSupport(request, null, errors, errorsProcessor);
    }

    @PutMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLIN_STATE_ACTION + "/canceled")
    @Validation(validators = { OrderSellinExistedValidator.class, WarehouseTotalItemExistedValidator.class })
    @Logged
    public ResponseEntity<?> stateCanceled(HttpServletRequest request, HttpServletResponse response, @RequestBody OrderSellinForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                Customer customer = sessionInfo.getCustomer();
                OrderSellinStatusController.this.doChangeStatus(form, OrderSellinStatus.CANCELED, getRpcResponse(), (BindingResult) errors, customer);
                getRpcResponse().addAttribute("message", "successful");
            }
        };

        return support.doSupport(request, null, errors, errorsProcessor);
    }

    @PostMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLIN_STATE_ACTION + "/finish")
    @Validation(validators = { OrderSellinExistedValidator.class, OrderSellinStateDeliveredExistedValidator.class })
    @Logged
    public ResponseEntity<?> stateFinish(HttpServletRequest request, HttpServletResponse response, @RequestBody OrderSellinForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                Customer customer = sessionInfo.getCustomer();
                if (customer != null) {
                    OrderSellinStatusController.this.doChangeStatus(form, OrderSellinStatus.FINISH, getRpcResponse(), (BindingResult) errors, customer);
                    getRpcResponse().addAttribute("message", "successful");
                }
            }
        };

        return support.doSupport(request, null, errors, errorsProcessor);
    }

    @PostMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLIN_STATE_ACTION + "/return")
    @Validation(validators = { OrderSellinWarehouseExistedValidator.class, OrderSellinStateDeliveredExistedValidator.class, WarehouseTotalItemExistedValidator.class })
    @Logged
    public ResponseEntity<?> stateReturn(HttpServletRequest request, HttpServletResponse response, @RequestBody OrderSellinForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                Customer customer = sessionInfo.getCustomer();
                OrderSellinStatusController.this.doChangeStatus(form, OrderSellinStatus.RETURN, getRpcResponse(), (BindingResult) errors, customer);
                getRpcResponse().addAttribute("message", "successful");

            }
        };

        return support.doSupport(request, null, errors, errorsProcessor);
    }

    @SuppressWarnings("rawtypes")
    protected void doChangeStatus(OrderSellinForm form, Enum state, RpcResponse rpcResponse, BindingResult errors, Customer customer) {
        OrderSellin orderSellin = this.orderSellinService.getActivated(form.getOrderSellinId());
        List<OrderSellinItem> orderSellinItems = this.orderSellinItemService.getAllBySellinId(form.getOrderSellinId());

        switch (state.toString()) {
            case "APPROVED":
                orderSellin.setOrderStatus(OrderSellinStatus.APPROVED.toString());
                this.stateApproved(orderSellin, orderSellinItems);
                break;
            case "DELIVERED":
                orderSellin.setOrderStatus(OrderSellinStatus.DELIVERED.toString());
                this.stateDelivered(orderSellin, orderSellinItems, form.getWarehouseId(), customer);
                break;
            case "CANCELED":
                String currentStatus = orderSellin.getOrderStatus();
                orderSellin.setCancelReason(form.getReason());
                orderSellin.setCancelType(form.getCancelType() != null ? form.getCancelType() : OrderSellinCancelType.OTHER);
                orderSellin.setOrderStatus(OrderSellinStatus.CANCELED.toString());
                orderSellin.setFinishDate(LocalDateTime.now());
                this.stateCanceled(orderSellin, orderSellinItems, currentStatus);
                break;
            case "FINISH":
                orderSellin.setOrderStatus(OrderSellinStatus.FINISH.toString());
                orderSellin.setFinishDate(LocalDateTime.now());
                this.stateFinish(orderSellin, orderSellinItems);
                break;
            case "RETURN":
                orderSellin.setCancelReason(form.getReason());
                orderSellin.setOrderStatus(OrderSellinStatus.RETURN.toString());
                orderSellin.setFinishDate(LocalDateTime.now());
                this.stateReturn(orderSellin, orderSellinItems, customer, form.getWarehouseId());
                break;
        }

        this.orderSellinService.updatePartial(orderSellin);

        return;
    }

    private void stateApproved(OrderSellin orderSellin, List<OrderSellinItem> orderSellinItems) {
        List<WarehouseTotalItem> warehouseTotalItems = new ArrayList<WarehouseTotalItem>();

        Map<Integer, WarehouseTotalItem> productWarehouseMap = new LinkedHashMap<Integer, WarehouseTotalItem>();
        for (OrderSellinItem item : orderSellinItems) {
            WarehouseTotalItem warehouseTotalItem = this.warehouseTotalItemService.getBySellinItemInfo(item.getProductId(), item.getProductVariationId());
            warehouseTotalItems.add(mapping(warehouseTotalItem, item));
            productWarehouseMap.put(item.getProductVariationId(), warehouseTotalItem);
        }
        
        List<OrderSellinPromotionLimitationDetailReward> rewards = rewardService.getByOrderId(orderSellin.getId());
        if (CollectionUtils.isNotEmpty(rewards)) {
            for (OrderSellinPromotionLimitationDetailReward reward : rewards) {
                WarehouseTotalItem warehouseTotalItem = productWarehouseMap.get(reward.getRewardProductVariationId()); 
                if (warehouseTotalItem == null) {
                    warehouseTotalItem = this.warehouseTotalItemService.getBySellinItemInfo(
                            reward.getRewardProductId(),
                            reward.getRewardProductVariationId());
                    warehouseTotalItems.add(warehouseTotalItem);
                }
                
                mappingOrderSellinPromotionLimitationDetailReward(warehouseTotalItem, reward);
            }
        }

        this.warehouseTotalItemService.saveAll(warehouseTotalItems);
        this.createStateChangeHistory(orderSellin, OrderSellinStatus.NEW.toString(), OrderSellinStatus.APPROVED.toString());
    }
    
    private WarehouseTotalItem mappingOrderSellinPromotionLimitationDetailReward(WarehouseTotalItem warehouseTotalItem,
            OrderSellinPromotionLimitationDetailReward reward) {
        Integer amountAvailable = warehouseTotalItem.getAmountAvailable();
        warehouseTotalItem.setAmountAvailable(amountAvailable - reward.getRewardAmount());
        warehouseTotalItem.setAmountInOrders(warehouseTotalItem.getAmountInOrders() + reward.getRewardAmount());
        return warehouseTotalItem;
    }

    private void stateDelivered(OrderSellin orderSellin, List<OrderSellinItem> orderSellinItems, Integer warehouseId, Customer customer) {
        
        this.createStateChangeHistory(orderSellin, OrderSellinStatus.APPROVED.toString(), OrderSellinStatus.DELIVERED.toString());
        
        // 1. Get warehouse
        Warehouse warehouse = this.warehouseService.get(warehouseId);
        
        // 2. Create Export Ticket
        WarehouseExportTicket exportTicket = createExportTicket(orderSellin, warehouseId, customer);
        
        // 3. Summing Order item and reward items
        Map<Integer, Integer> itemCounter = new HashMap<>();
        Map<Integer, Object> productMap = new HashMap<>();
        ServiceUtils.getInstance().summingOrderSellinItemAndRewardItem(rewardService, orderSellin, orderSellinItems, itemCounter, productMap);

        // 4. Create Export Ticket Items
        List<WarehouseExportTicketItem> items = new ArrayList<>();
        for (Map.Entry<Integer, Object> entry : productMap.entrySet()) {
            WarehouseExportTicketItem item = this.createExportTicketItem(exportTicket, itemCounter, entry);
            items.add(item);
        }
        
        // 5. Update warehouse
        this.createOrUpdateRetailerWarehouseItem(exportTicket, warehouse, orderSellin, items);
    }

    private void createOrUpdateRetailerWarehouseItem(WarehouseExportTicket ticket, Warehouse warehouse, OrderSellin orderSellin,
            List<WarehouseExportTicketItem> items) {
        
        List<WarehouseTotalItem> warehouseTotalItems = new ArrayList<>();
        List<WarehouseItem> warehouseItems = new ArrayList<>();
        List<WarehouseItemHistory> histories = new ArrayList<>();
        
        for (WarehouseExportTicketItem item : items) {
            // update RetailerWarehouseTotalItem
            WarehouseTotalItem whTotalItem = this.warehouseTotalItemService.getBySellinItemInfo(
                    item.getProductId(), item.getProductVariationId());
            
            whTotalItem.setAmountExport(whTotalItem.getAmountExport() + item.getAmount());
            whTotalItem.setAmountInOrders(whTotalItem.getAmountInOrders() - item.getAmount());
            whTotalItem.setAmount(whTotalItem.getAmount() - item.getAmount());
            
            warehouseTotalItems.add(whTotalItem);
            
            // update WarehouseItem
            WarehouseItem whItem = this.warehouseItemService.getBySellinItemInfo(warehouse.getId(), item.getProductId(), item.getProductVariationId());
            
            Integer oldAmount = whItem.getAmount();
            whItem.setAmount(oldAmount - item.getAmount());
            warehouseItems.add(whItem);

            // create RetailerWarehouseItemHistory
            WarehouseItemHistory history = createRetailerWarehouseItemHistory(orderSellin, whItem, item, ticket, whItem.getId(), oldAmount);
            histories.add(history);
            
            // update RetailerWarehouseDailyRemain
            WarehouseDailyRemain rWhDailyRemain = this.warehouseDailyRemainService.getInWarehouseByProductInfo(
                    warehouse.getId(), item.getProductId(), item.getProductVariationId());
            
            if (rWhDailyRemain == null) {
                rWhDailyRemain = new WarehouseDailyRemain();
                rWhDailyRemain.setWarehouseId(warehouse.getId());
                rWhDailyRemain.setProductId(item.getProductId());
                rWhDailyRemain.setProductVariationId(item.getProductVariationId());
                rWhDailyRemain.setSku(item.getSku());
                rWhDailyRemain.setAmount(0);
                rWhDailyRemain.setAmountImport(0);
            }
            
            rWhDailyRemain.setAmountExport(rWhDailyRemain.getAmountExport() + item.getAmount());
            rWhDailyRemain.setAmount(rWhDailyRemain.getAmount() - item.getAmount());
            rWhDailyRemain = this.warehouseDailyRemainService.saveOrUpdate(rWhDailyRemain);
        }
        
        this.warehouseTotalItemService.saveAll(warehouseTotalItems);
        this.warehouseItemService.saveAll(warehouseItems);
        this.warehouseItemHistoryService.saveAll(histories);
    }

    private WarehouseItemHistory createRetailerWarehouseItemHistory(OrderSellin orderSellin, WarehouseItem whItem, WarehouseExportTicketItem item,
            WarehouseExportTicket ticket, Integer warehouseItemId, Integer oldAmount) {
        WarehouseItemHistory history = new WarehouseItemHistory();
        history.setWarehouseId(whItem.getWarehouseId());
        history.setWarehouseItemId(warehouseItemId);
        history.setProductId(whItem.getProductId());
        history.setProductVariationId(whItem.getProductVariationId());
        history.setSku(whItem.getSku());
        history.setChangeDate(LocalDateTime.now());
        history.setChangeType(RetailerWarehouseItemHistory.CHANGE_TYPE_EXPORT);
        history.setOldAmount(oldAmount);
        history.setChangeAmount(item.getAmount());
        history.setAmount(oldAmount - item.getAmount());
        history.setExportTicketId(ticket.getId());
        history.setExportTicketCode(ticket.getExportTicketCode());
        history.setOrderSelloutId(orderSellin.getId());
        history.setOrderSelloutCode(orderSellin.getOrderCode());
        history.setExtraData(new LinkedHashMap<>());

        return history;
    }

    private WarehouseExportTicketItem createExportTicketItem(WarehouseExportTicket ticket, Map<Integer, Integer> itemCounter,
            Entry<Integer, Object> entry) {
        WarehouseExportTicketItem item = new WarehouseExportTicketItem();
        item.setWarehouseId(ticket.getWarehouseId());
        item.setExportTicketId(ticket.getId());
        item.setExportTicketCode(ticket.getExportTicketCode());
        item.setStatus(Status.ACTIVE);
        
        Object value = entry.getValue();
        if (value instanceof OrderSellinItem) {
            OrderSellinItem orderItem = (OrderSellinItem) value;
            item.setProductId(orderItem.getProductId());
            item.setProductVariationId(orderItem.getProductVariationId());
            item.setSku(orderItem.getSku());
            
            item.setPrice(orderItem.getUnitPrice());
            item.setTotal(orderItem.getTotalCost());
        } else {
            OrderSellinPromotionLimitationDetailReward reward = (OrderSellinPromotionLimitationDetailReward) value;
            item.setProductId(reward.getRewardProductId());
            item.setProductVariationId(reward.getRewardProductVariationId());
            ProductVariation pv = productVariationService.get(reward.getRewardProductVariationId());
            item.setSku(pv.getSku());
            item.setAmount(reward.getRewardAmount());
            item.setPrice(reward.getUnitPrice());
            item.setTotal(reward.getRewardValue());
        }
      
        item.setAmount(itemCounter.get(item.getProductVariationId()));
        item = this.warehouseExportTicketItemService.create(item);
        
        return item;
    }

    /**
     * @param orderSellin
     * @param warehouseId
     * @param customer
     * @return
     */
    private WarehouseExportTicket createExportTicket(OrderSellin orderSellin, Integer warehouseId, Customer customer) {
        WarehouseExportTicket exportTicket = new WarehouseExportTicket();
        exportTicket.setWarehouseId(warehouseId);
        exportTicket.setExportTicketCode(EXPORT_TICKET_CODE_PREFIX + System.currentTimeMillis());
        exportTicket.setExportType(0);
        exportTicket.setExportPerson(customer.getFullname());
        exportTicket.setExportDate(LocalDateTime.now());
        exportTicket.setTotal(orderSellin.getFinalCost());
        exportTicket.setExtraData(new LinkedHashMap<>());
        exportTicket.setStatus(Status.ACTIVE);

        exportTicket = this.warehouseExportTicketService.create(exportTicket);
        exportTicket.setExportTicketCode(CommonUtils.generateCode(EXPORT_TICKET_CODE_PREFIX, exportTicket.getWarehouseId(),
                EXPORT_TICKET_CODE_MIDDLE, exportTicket.getId()));
        this.warehouseExportTicketService.updatePartial(exportTicket);
        return exportTicket;
    }

    private void stateCanceled(OrderSellin orderSellin, List<OrderSellinItem> orderSellinItems, String currentStatus) {
        List<WarehouseTotalItem> warehouseTotalItems = new ArrayList<WarehouseTotalItem>();

        if (!OrderSellinStatus.NEW.toString().equalsIgnoreCase(currentStatus)) {
            for (OrderSellinItem item : orderSellinItems) {
                WarehouseTotalItem warehouseTotalItem = this.warehouseTotalItemService.getBySellinItemInfo(item.getProductId(), item.getProductVariationId());
                warehouseTotalItem.setAmountInOrders(warehouseTotalItem.getAmountInOrders() - item.getTotalAmount());
                warehouseTotalItem.setAmountAvailable(warehouseTotalItem.getAmountAvailable() + item.getTotalAmount());
                warehouseTotalItems.add(warehouseTotalItem);
            }
            this.warehouseTotalItemService.saveAll(warehouseTotalItems);
        }       
        
        this.createStateChangeHistory(orderSellin, currentStatus, OrderSellinStatus.CANCELED.toString());
    }

    private void stateFinish(OrderSellin orderSellin, List<OrderSellinItem> orderSellinItems) {
        this.createStateChangeHistory(orderSellin, OrderSellinStatus.DELIVERED.toString(), OrderSellinStatus.FINISH.toString());
        this.rwhImportTicketService.createFromOrderSellin(orderSellin);
    }

    private void stateReturn(OrderSellin orderSellin, List<OrderSellinItem> items, Customer customer, int warehouseId) {
        this.createStateChangeHistory(orderSellin, OrderSellinStatus.DELIVERED.toString(), OrderSellinStatus.RETURN.toString());
        
        WarehouseImportTicket ticket = this.createImportTicket(orderSellin, items, customer, warehouseId);
        
        List<WarehouseImportTicketItem> ticketItems = this.createWarehouseImportTicketItems(orderSellin, ticket, items);
        
        this.createOrUpdateWarehouseItems(orderSellin, ticketItems);
    }
    
    private List<WarehouseItem> createOrUpdateWarehouseItems(OrderSellin order, List<WarehouseImportTicketItem> importItems) {
        List<WarehouseItem> warehouseItems = new ArrayList<WarehouseItem>();
        
        for (WarehouseImportTicketItem importItem : importItems) {
            Integer warehouseId = importItem.getWarehouseId();
            Integer productId = importItem.getProductId();
            Integer productVariationId =importItem.getProductVariationId();
            String sku = importItem.getSku();
            Integer amount = importItem.getAmount();
            Integer oldAmount = 0;
            WarehouseItem item = this.warehouseItemService.getByWarehouseImportTicketItemInfo(warehouseId, productId, productVariationId, sku);
                                                                                                
            if (item != null) {
                //update total amount
                oldAmount = item.getAmount();
                item.setAmount(oldAmount + amount);
                item = this.warehouseItemService.updatePartial(item);
            } else {
                // create new warehouse record
                item = this.warehouseItemService.create(new WarehouseItem(warehouseId, productId, productVariationId, sku, amount));
            }
            
            warehouseItems.add(item);
            
            //create or update history
            createOrUpdateWarehouseItemHistory(order, item.getId(), importItem, oldAmount);
            createOrUpdateWarehouseTotalItem(warehouseId, productId, productVariationId, sku, amount);
            createOrUpdateWarehouseDailyRemain(warehouseId, productId, productVariationId, sku, amount);
        }
        
        return warehouseItems;
    }
    
    private void createOrUpdateWarehouseDailyRemain(Integer warehouseId, Integer productId, Integer productVariationId, String sku, Integer amount) {
        WarehouseDailyRemain item = this.warehouseDailyRemainService.getInWarehouseByProductInfo(warehouseId, productId, productVariationId);
        if (item == null) {
            item = new WarehouseDailyRemain();
            item.setLogDate(LocalDate.now());
            item.setWarehouseId(warehouseId);
            item.setProductId(productId);
            item.setProductVariationId(productVariationId);
            item.setSku(sku);
            item.setAmount(0);
            item.setAmountToday(0);
            item.setAmountImport(0);
            item.setAmountExport(0);;
        }

        item.setAmount(amount + item.getAmount());
        item.setAmountImport(item.getAmountImport() + amount);

        this.warehouseDailyRemainService.saveOrUpdate(item);
    }

    private void createOrUpdateWarehouseTotalItem(Integer warehouseId, Integer productId, Integer productVariationId, String sku, Integer amount) {
        WarehouseTotalItem item = this.warehouseTotalItemService.getBySellinItemInfo(productId, productVariationId);
        
        if (item == null) {
            item = new WarehouseTotalItem();
            item.setSku(sku);
            item.setProductId(productId);
            item.setProductVariationId(productVariationId);
            item.setAmount(0);
            item.setAmountAvailable(0);
            item.setAmountInOrders(0);
            item.setAmountImport(0);
            item.setAmountExport(0);
        }

        item.setAmount(item.getAmount() + amount);
        item.setAmountAvailable(item.getAmountAvailable() + amount);
        item.setAmountImport(item.getAmountImport() + amount);

        this.warehouseTotalItemService.saveOrUpdate(item);
    }

    private WarehouseImportTicket createImportTicket(OrderSellin orderSellin, List<OrderSellinItem> orderSellinItems, Customer customer, int warehouseId) {
        WarehouseImportTicket ticket = new WarehouseImportTicket();
        ticket.setWarehouseId(warehouseId);
        ticket.setImportTicketCode(WarehouseImportTicketController.IMPORT_TICKET_CODE_PREFIX + System.currentTimeMillis());
        ticket.setImportType(ImportType.COMPANY);
        ticket.setImportPerson(customer.getFullname());
        ticket.setImportDate(LocalDateTime.now());
        ticket.setApprovedPerson(customer.getFullname());
        ticket.setDescription("Phiếu nhập kho từ đơn hàng bị trả: " + orderSellin.getOrderCode());
        ticket.setTotal(orderSellin.getFinalCost());
        
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("orderSellinId", orderSellin.getId());
        map.put("orderCode", orderSellin.getOrderCode());
        map.put("orderStatus", orderSellin.getOrderStatus());
        
        ticket.setExtraData(map);
        ticket.setStatus(Status.ACTIVE);
        
        ticket = this.warehouseImportTicketService.create(ticket);
        
        ticket.setImportTicketCode(CommonUtils.generateCode(
                WarehouseImportTicketController.IMPORT_TICKET_CODE_PREFIX, ticket.getWarehouseId(),
                WarehouseImportTicketController.IMPORT_TICKET_CODE_MIDDLE, ticket.getId()));
        
        ticket.setTotal(orderSellin.getFinalCost());
        ticket = this.warehouseImportTicketService.updatePartial(ticket);
        return ticket;
    }
    
    private List<WarehouseImportTicketItem> createWarehouseImportTicketItems(OrderSellin orderSellin, WarehouseImportTicket ticket, List<OrderSellinItem> items) {
        List<WarehouseImportTicketItem> ticketItems = new ArrayList<WarehouseImportTicketItem>();
        
        Map<Integer, Integer> itemCounter = new HashMap<>();
        Map<Integer, Object> productMap = new HashMap<>();
        ServiceUtils.getInstance().summingOrderSellinItemAndRewardItem(rewardService, orderSellin, items, itemCounter, productMap);
        
        for (Map.Entry<Integer, Object> entry : productMap.entrySet()) {
            WarehouseImportTicketItem ticketItem = this.createWarehouseImportTicketItem(ticket, itemCounter, entry);
            ticketItems.add(ticketItem);
        }
        
        return ticketItems;
    }

    private WarehouseImportTicketItem createWarehouseImportTicketItem(WarehouseImportTicket ticket, Map<Integer, Integer> itemCounter, Map.Entry<Integer, Object> entry) {
        WarehouseImportTicketItem item = new WarehouseImportTicketItem();
        item.setWarehouseId(ticket.getWarehouseId());
        item.setImportTicketId(ticket.getId());
        item.setImportTicketCode(ticket.getImportTicketCode());
        item.setStatus(Status.ACTIVE);
        
        Object value = entry.getValue();
        if (value instanceof OrderSellinItem) {
            OrderSellinItem orderItem = (OrderSellinItem) value;
            item.setProductId(orderItem.getProductId());
            item.setProductVariationId(orderItem.getProductVariationId());
            item.setSku(orderItem.getSku());
            item.setIncomePrice(orderItem.getUnitPrice());
            item.setTotal(orderItem.getTotalCost());
        } else if (value instanceof OrderSellinPromotionLimitationDetailReward) {
            OrderSellinPromotionLimitationDetailReward reward = (OrderSellinPromotionLimitationDetailReward) value;
            item.setProductId(reward.getRewardProductId());
            item.setProductVariationId(reward.getRewardProductVariationId());
            ProductVariation pv = productVariationService.get(reward.getRewardProductVariationId());
            item.setSku(pv.getSku());
            item.setAmount(reward.getRewardAmount());
            item.setIncomePrice(reward.getUnitPrice());
            item.setTotal(reward.getRewardValue());
        }
      
        item.setAmount(itemCounter.get(item.getProductVariationId()));
        item = this.warehouseImportTicketItemService.create(item);
        
        return item;
    }

    /**
     * @param warehouseImportTicketItem
     */
    private void createOrUpdateWarehouseItemHistory(OrderSellin order, Integer warehouseItemId, WarehouseImportTicketItem warehouseImportTicketItem, Integer oldAmount) {
        Integer amount = warehouseImportTicketItem.getAmount();
        
        WarehouseItemHistory warehouseItemHistory = new WarehouseItemHistory();
        String props = "warehouseId,productId,productVariationId,sku,amount,importTicketCode,importTicketId";
        SystemUtils.getInstance().copyProperties(warehouseImportTicketItem, warehouseItemHistory, props.split(","));
        warehouseItemHistory.setWarehouseItemId(warehouseItemId);
        warehouseItemHistory.setChangeDate(LocalDateTime.now());
        warehouseItemHistory.setAmount(amount + oldAmount);
        warehouseItemHistory.setChangeType(WarehouseItemHistory.CHANGE_TYPE_ADDITINAL);
        warehouseItemHistory.setChangeAmount(amount);
        warehouseItemHistory.setOldAmount(oldAmount);
        warehouseItemHistory.setOrderSellinId(order.getId());
        warehouseItemHistory.setOrderSellinCode(order.getOrderCode());
        warehouseItemHistory.setExtraData(new LinkedHashMap<>());
        System.err.println("warehouseItemHistory " + warehouseItemHistory);
        this.warehouseItemHistoryService.create(warehouseItemHistory);
    }
    
    /**
     * @param warehouseId
     * @param productId
     * @param productVariationId
     * @param amount
     */
    @SuppressWarnings("unused")
	private void createWarehouseTotalItem(Integer productId, Integer productVariationId, String sku, Integer amount, Integer totalAmount) {
        WarehouseTotalItem warehouseTotalItem = this.warehouseTotalItemService.getByWarehouseImportTicketItemInfo(productId, productVariationId, sku);
        if (warehouseTotalItem == null) {
            warehouseTotalItem = new WarehouseTotalItem();
            warehouseTotalItem.setSku(sku);
            warehouseTotalItem.setProductId(productId);
            warehouseTotalItem.setProductVariationId(productVariationId);
        }
        warehouseTotalItem.setAmount(totalAmount);
        warehouseTotalItem.setAmountAvailable(totalAmount - warehouseTotalItem.getAmountInOrders());
        warehouseTotalItem.setAmountImport(warehouseTotalItem.getAmountImport() + amount);
        this.warehouseTotalItemService.saveOrUpdate(warehouseTotalItem);
    }

    private WarehouseTotalItem mapping(WarehouseTotalItem warehouseTotalItem, OrderSellinItem item) {
        Integer amountAvailable = warehouseTotalItem.getAmountAvailable();
        warehouseTotalItem.setAmountAvailable(amountAvailable - item.getTotalAmount());
        warehouseTotalItem.setAmountInOrders(warehouseTotalItem.getAmountInOrders() + item.getTotalAmount());
        return warehouseTotalItem;
    }

    private OrderSellinStateChangeHistory createStateChangeHistory(OrderSellin orderSellin, String fromState, String toState) {
        OrderSellinStateChangeHistory history = new OrderSellinStateChangeHistory();
        history.setSellinOrderId(orderSellin.getId());
        history.setSellinOrderCode(orderSellin.getOrderCode());
        history.setChangeDate(LocalDateTime.now());
        history.setFromState(fromState);
        history.setToState(toState);
        history = this.osiStateChangeHistoryService.create(history);
        return history;
    }

}
