package com.mljr.commodity.dto;

import com.mljr.b2c.commodity.api.enums.AutoRepriceStrategyEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Sku implements Serializable{
	
    /**
	 * Creates a new instance of Sku1.
	 *
	 */
	public Sku() {}
	

	/**
	 * Creates a new instance of Sku.
	 *
	 * @param id
	 * @param store
	 */
	public Sku(Integer id, Integer store) {
		this.id = id;
		this.store = store;
	}


	/**
	 * Creates a new instance of Sku.
	 *
	 * @param jdSkuId
	 * @param status
	 */
	public Sku(Long jdSkuId, short status) {
		this.jdSkuId = jdSkuId;
		this.status = status;
	}


	/**
	 * Creates a new instance of Sku.
	 *
	 * @param jdSkuId
	 * @param price
	 * @param jdPrice
	 * @param ourPrice
	 */
	public Sku(Long jdSkuId, BigDecimal price, BigDecimal jdPrice, BigDecimal ourPrice) {
		this.jdSkuId = jdSkuId;
		this.price = price;
		this.jdPrice = jdPrice;
		this.ourPrice = ourPrice;
	}

	/**
	 * Creates a new instance of Sku.
	 *
	 * @param id
	 * @param imageBackup
	 * @param introBackup
	 */
	public Sku(Integer id, String imageBackup, String introBackup) {
		this.id = id;
		this.imageBackup = imageBackup;
		this.introBackup = introBackup;
	}

	/**
	 * Creates a new instance of Sku1.
	 *
	 * @param jdSkuId
	 * @param name
	 * @param price
	 * @param jdPrice
	 * @param ourprice
	 * @param status
	 * @param createTime
	 * @param brandName
	 * @param spuId
	 * @param saleAttr
	 * @param introduction
	 * @param param
	 * @param imagePath
	 * @param wareQd
	 */
	public Sku(Long jdSkuId, String name, BigDecimal price, BigDecimal jdPrice, BigDecimal ourprice, Short status,
               Date createTime, String brandName, Integer spuId, String saleAttr, String introduction, String param,
               String imagePath, String wareQd) {
		super();
		this.jdSkuId = jdSkuId;
		this.name = name;
		this.price = price;
		this.jdPrice = jdPrice;
		this.ourPrice = ourprice;
		this.status = status;
		this.createTime = createTime;
		this.brandName = brandName;
		this.spuId = spuId;
		this.saleAttr = saleAttr;
		this.introduction = introduction;
		this.param = param;
		this.imagePath = imagePath;
		this.wareQd = wareQd;
        this.autoReprice = AutoRepriceStrategyEnum.AUTO_DISABLE.getValue();
        this.priceDelta = BigDecimal.ZERO;
        this.pricePercent = BigDecimal.ZERO;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Long jdSkuId;

    private String name;

    private BigDecimal price;

    private BigDecimal jdPrice;

    private BigDecimal ourPrice;

    private Short status;

    private Date createTime;

    private Byte label;

    private String advertisementWord;

    private Date forsaleTime;

    private Date suspensionTime;

    private String brandName;

    private Integer spuId;

    private String saleAttr;
    
    private String introduction;

    private String param;

    private String imagePath;
    
    private String images;

    private String wareQd;
    
    private String imageBackup;
    
    private String introBackup;

    private Short autoReprice;

    private BigDecimal priceDelta;

    private BigDecimal pricePercent;

    private Integer store;  //库存

    private Short channel;      //渠道

    private BigDecimal marketPrice; //吊牌价

    public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getWareQd() {
		return wareQd;
	}

	public void setWareQd(String wareQd) {
		this.wareQd = wareQd;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getJdSkuId() {
        return jdSkuId;
    }

    public void setJdSkuId(Long jdSkuId) {
        this.jdSkuId = jdSkuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getJdPrice() {
        return jdPrice;
    }

    public void setJdPrice(BigDecimal jdPrice) {
        this.jdPrice = jdPrice;
    }

    public BigDecimal getOurPrice() {
        return ourPrice;
    }

    public void setOurPrice(BigDecimal ourprice) {
        this.ourPrice = ourprice;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getLabel() {
        return label;
    }

    public void setLabel(Byte label) {
        this.label = label;
    }

    public String getAdvertisementWord() {
        return advertisementWord;
    }

    public void setAdvertisementWord(String advertisementWord) {
        this.advertisementWord = advertisementWord == null ? null : advertisementWord.trim();
    }

    public Date getForsaleTime() {
        return forsaleTime;
    }

    public void setForsaleTime(Date forsaleTime) {
        this.forsaleTime = forsaleTime;
    }

    public Date getSuspensionTime() {
        return suspensionTime;
    }

    public void setSuspensionTime(Date suspensionTime) {
        this.suspensionTime = suspensionTime;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public Integer getSpuId() {
        return spuId;
    }

    public void setSpuId(Integer spuId) {
        this.spuId = spuId;
    }

    public String getSaleAttr() {
        return saleAttr;
    }

    public void setSaleAttr(String saleAttr) {
        this.saleAttr = saleAttr == null ? null : saleAttr.trim();
    }


	/**
	 * imageBackup.
	 *
	 * @return  the imageBackup
	 * @since   JDK 1.8
	 */
	public String getImageBackup() {
		return imageBackup;
	}


	/**
	 * introBackup.
	 *
	 * @return  the introBackup
	 * @since   JDK 1.8
	 */
	public String getIntroBackup() {
		return introBackup;
	}


	/**
	 * imageBackup.
	 *
	 * @param   imageBackup    the imageBackup to set
	 * @since   JDK 1.8
	 */
	public void setImageBackup(String imageBackup) {
		this.imageBackup = imageBackup;
	}


	/**
	 * introBackup.
	 *
	 * @param   introBackup    the introBackup to set
	 * @since   JDK 1.8
	 */
	public void setIntroBackup(String introBackup) {
		this.introBackup = introBackup;
	}

    public Short getAutoReprice() {
        return autoReprice;
    }

    public void setAutoReprice(Short autoReprice) {
        this.autoReprice = autoReprice;
    }

    public BigDecimal getPriceDelta() {
        return priceDelta;
    }

    public void setPriceDelta(BigDecimal priceDelta) {
        this.priceDelta = priceDelta;
    }

    public BigDecimal getPricePercent() {
        return pricePercent;
    }

    public void setPricePercent(BigDecimal pricePercent) {
        this.pricePercent = pricePercent;
    }


	/**
	 * images.
	 *
	 * @return  the images
	 * @since   JDK 1.8
	 */
	public String getImages() {
		return images;
	}


	/**
	 * images.
	 *
	 * @param   images    the images to set
	 * @since   JDK 1.8
	 */
	public void setImages(String images) {
		this.images = images;
	}

    public Short getChannel() {
        return channel;
    }

    public void setChannel(Short channel) {
        this.channel = channel;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }
}